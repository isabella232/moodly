'use strict';

// Declare here that angular is the US version - other locales can be easily substituted.

require([ 'angular', 'angular-route', 'angular-resource' ], function (angular) {

    function MoodlyCtrl($scope, $http) {
        $scope.createMoodly = function(data) {
            $http.post('/rest/moodlies', {
                intervalDays: parseInt($scope.moodly.interval)
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
                templateUrl: 'partials/moodlies.html',
                controller: MoodlyCtrl,
                reloadOnSearch: false
            });
            $routeProvider.otherwise({redirectTo: '/'});
        }]);

    angular.bootstrap(document, ['moodly']);
});
