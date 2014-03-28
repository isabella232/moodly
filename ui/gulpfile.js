var gulp = require('gulp');
var browserify = require('gulp-browserify');
var concat = require('gulp-concat');
var gulpFilter = require('gulp-filter');
var gutil = require('gulp-util');
var uglify = require('gulp-uglify');
var clean = require('gulp-clean');
var jshint = require('gulp-jshint-cached');

var react = require('gulp-react');
var mocha = require('gulp-mocha');

var paths = {
    src: './src',
    dest: '../public/react',
    target: './target'
};

var libs = ['react', 'jquery', 'es5-shim-sham'];

var modules = ['admin'];
var noop = function () {};

gulp.task('clean', function() {
    gulp.src(paths.target, {read: false})
        .pipe(clean());
});

var jshintModule = function(module, cb) {
    // first transform JSX syntax into JavaScript to make jslint happy
    gulp.src(paths.src + '/modules/' + module + '/**/*.js')
        .pipe(react())
        .pipe(jshint.cached())
        .pipe(jshint.reporter('default'));
    cb();
};

gulp.task('lint', function() {
    modules.forEach(function(module) { jshintModule(module, noop); });
});

// package one module
var buildModule = function(module, cb) {
    gutil.log("building module '" + module + "'");
    gulp.src(paths.src + '/modules/' + module + '/app/main.js')
        .pipe(browserify({
            debug: !gutil.env.production,
            transform: ['reactify'],
            external: libs,
            exclude: libs
        }))
        .pipe(gutil.env.production ? uglify() : gutil.noop())
        .pipe(concat(module + '.js'))
        .pipe(gulp.dest(paths.dest));
    cb();
};

gulp.task('js', function() {
    modules.forEach(function(module) { buildModule(module, noop); });
});

var buildLib = function(lib, cb) {
    var externals = libs.filter(function(l) {return l !== lib});
    gutil.log("building lib '" + lib + "'");
    gulp.src(paths.src + '/libs/' + lib + '.js')
        .pipe(browserify({ require: libs, external: externals }))
        .pipe(gutil.env.production ? uglify() : gutil.noop())
        .pipe(gulp.dest(paths.dest));
};

gulp.task('lib', function () {
    libs.forEach(function(lib) { buildLib(lib, noop); });
});

gulp.task('build', ['lint', 'js', 'lib']);

var testModule = function(module, cb) {
    // tests are running in nodes and therefore do not need browserify
    var testFilter = gulpFilter('test/**/*.js');

    gutil.log("testing module '" + module + "'");
    gulp.src([paths.src + '/modules/' + module + '/**/*.js'])

        // transform jsx to js
        .pipe(react())

        // copy into target
        .pipe(gulp.dest(paths.target + '/' + module))

        // run mocha
        .pipe(testFilter)
        .pipe(mocha({}))
        .on('error', console.warn.bind(console));
    cb()
};

gulp.task('test', function () {
    modules.forEach(function(module) { testModule(module, noop); });
});

gulp.task('watch', function() {
    modules.forEach(function (module) {
        gulp.watch(paths.src + '/modules/' + module + '/**', function () {
            jshintModule(module, function() {
                testModule(module, function() {
                    buildModule(module, noop);
                });
            });
        });
    });

    gulp.watch(paths.src + '/libs/**', ['lib:external']);
});

gulp.task('default', ['build', 'test']);
