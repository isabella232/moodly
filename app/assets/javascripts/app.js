'use strict';

// Declare here that angular is the US version - other locales can be easily substituted.

require([ 'angular', 'angular-route', 'angular-resource' ], function (angular) {

    function MoodlyCtrl($scope, $http, $location) {
        $scope.moodlyUrl = '';
        $scope.baseUrl = $location.protocol() + "://" + $location.host() + ":" + $location.port();
        console.log($location);
        $scope.moodly = {};
        $scope.moodly.interval = 7;
        $scope.createMoodly = function(data) {
            $http.post('/rest/moodlies', {
                intervalDays: parseInt($scope.moodly.interval)
            }).success(function(data) {
                $scope.moodlyUrl = '/moodly/' + data.id;
            });
        };
    }

    function VotesCtrl($scope) {
        // show votes ...
    }

    angular.module('moodly', ['ngRoute', 'ngResource' ]).
        controller('MoodlyCtrl', ['$scope', function($scope) {
            console.log('MoodlyCtrl');
        }]).
        config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/moodlies.html',
                controller: MoodlyCtrl,
                reloadOnSearch: false
            }).when('/votes', {
                templateUrl: '',
                controller: VotesCtrl,
                reloadOnSearch: false
            });
            $routeProvider.otherwise({redirectTo: '/'});
        }]);

    angular.bootstrap(document, ['moodly']);
});
