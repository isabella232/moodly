// `main.js` is the file that sbt-web will use as an entry point
(function (requirejs) {
    'use strict';

    // -- RequireJS config --
    requirejs.config({
        paths: {
            'angular': ['../lib/angularjs/angular'],
            'angular-route': ['../lib/angularjs/angular-route'],
            'angular-cookies': ['../lib/angularjs/angular-cookies'],
            'angular-resource': ['../lib/angularjs/angular-resource'],
            'underscorejs': ['../lib/underscorejs/underscore']
        },
        shim: {
            'angular': {
                exports: 'angular'
            },
            'angular-route': ['angular'],
            'angular-cookies': ['angular'],
            'angular-resource': ['angular']
        }
    });

    requirejs.onError = function (err) {
        console.log(err);
    };

    // Load the app. This is kept minimal so it doesn't need much updating.
    require(['angular', 'angular-route', 'angular-resource', 'angular-cookies', './moodly'], function (angular) {
        angular.bootstrap(document, ['moodly']);
    });
})(requirejs);