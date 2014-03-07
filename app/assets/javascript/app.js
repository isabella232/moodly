'use strict';

// Declare here that angular is the US version - other locales can be easily substituted.

define('angular', ['webjars!angular.js', 'webjars!angular-route.js', 'webjars!angular-resource.js'], function () {
    return angular;
});

requirejs.config({
    shim: {
        'webjars!angular.js': ['webjars!angular.js'],
        'webjars!angular-route.js': ['webjars!angular-route.js'],
        'webjars!angular-resource.js': ['webjars!angular-resource.js']
    }
});

require([ 'angular', 'views/moodlies' ], function (angular) {

    function MoodlyCtrl($scope, $http) {
        $scope.createMoodly = function(data) {
            $http.post('/rest/moodlies', {
                interval: $scope['moodly.interval']
            }).success(function() {
            });
        };
    }

    angular.module('moodly', ['ngRoute', 'ngResource' ]).
        controller('MoodlyCtrl', ['$scope', function($scope) {
            console.log('MoodlyCtrl');
        }]).
        config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/votes', {
                templateUrl: '/views/moodlies.html',
                controller: MoodlyCtrl,
                reloadOnSearch: false
            });
            $routeProvider.otherwise({redirectTo: '/'});
        }]);

    angular.bootstrap(document, ['moodly']);
});
