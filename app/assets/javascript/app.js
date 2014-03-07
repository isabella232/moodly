'use strict';

// Declare here that angular is the US version - other locales can be easily substituted.

define('angular', ['webjars!angular.js', 'webjars!angular-route.js', 'webjars!angular-resource.js'], function () {
    return angular;
});

requirejs.config({
    shim: {
        'webjars!angular-route.js': ['webjars!angular.js'],
        'webjars!angular-resource.js': ['webjars!angular.js']
    }
});

require([ 'angular' ], function (angular) {

    angular.module('moodly', ['ngRoute', 'ngResource' ]).
        config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/votes', {templateUrl: '/config/projects', reloadOnSearch: false});
            $routeProvider.otherwise({redirectTo: '/'});
        }]);

    angular.bootstrap(document, ['moodly']);
});
