'use strict';

// Declare here that angular is the US version - other locales can be easily substituted.

require([ 'angular', 'angular-route', 'angular-resource', 'angular-cookies'], function (angular) {

    function MoodlyCtrl($scope, $http, $location) {
        console.log('MoodlyCtrl');
        $scope.moodlyUrl = '';
        $scope.baseUrl = $location.protocol() + "://" + $location.host() + ":" + $location.port();
        console.log($location);
        $scope.moodly = {};
        $scope.moodly.interval = 7;
        $scope.createMoodly = function(data) {
            $http.post('/rest/moodlies', {
                intervalDays: parseInt($scope.moodly.interval)
            }).success(function(data) {
                //$scope.moodlyUrl = '/#/voting/' + data.id;
                $scope.moodlyUrl = '/#/voting/' + data.id;
            });
        };
    }

    function VotingCtrl($scope, $routeParams, $resource, $cookieStore, $window) {

        $resource('/rest/moodlies/:moodlyId/currentIterationCount', {moodlyId:$routeParams.id}).get(function(data) {
            console.log("currentIterationCount=" + data[0]);
            var cookieKey = "moodly-" + $routeParams.id + "-" + data[0];

            if ($cookieStore.get(cookieKey)) {
                console.log("user has cookie!");
                $window.location.hash = "/voted/" + $routeParams.id + "/TODO";
            }

            // TODO: this is a minor race condition
            $scope.vote = function(ballot) {
                var Ballot = $resource('/rest/moodlies/:moodlyId/ballots', {moodlyId:$routeParams.id},{});
                if (!$cookieStore.get(cookieKey)) {
                    $cookieStore.put(cookieKey, Math.random());
                }
                Ballot.save({cookieId:$cookieStore.get(cookieKey), vote:ballot});
                console.log(ballot);
                $window.location.hash = "/voted/" + $routeParams.id + "/" + ballot;
            };
        });

    }

    function VotedCtrl($scope, $routeParams, $resource) {
        $scope.ballot = $routeParams.ballot
//        var Ballot = $resource('/rest/moodlies/:moodlyId/ballots', {moodlyId:$routeParams.id},
//                {
//                    get : {
//                      method: 'GET',
//                      isArray: true
//                    }
//                }
//        );
//        Ballot.get();
    }

    angular.module('moodly', ['ngRoute', 'ngResource', 'ngCookies' ]).
        config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/', {
                templateUrl: 'partials/moodlies.html',
                controller: MoodlyCtrl,
                reloadOnSearch: false
            }).when('/voting/:id', {
                templateUrl: 'partials/voting.html',
                controller: VotingCtrl,
                reloadOnSearch: false
            }).when('/voted/:id/:ballot', {
                        templateUrl: 'partials/voted.html',
                        controller: VotedCtrl,
                        reloadOnSearch: false
            }).otherwise({redirectTo: '/'});
        }]);

    angular.bootstrap(document, ['moodly']);
});
