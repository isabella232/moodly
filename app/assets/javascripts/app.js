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

    function VotingCtrl($scope, $routeParams, $resource, $cookieStore, $window, CookieService) {

        $resource('/rest/moodlies/:moodlyId/currentIterationCount', {moodlyId:$routeParams.id}).get(function(data) {
            console.log("currentIterationCount=" + data[0]);
            var cookieKey = "moodly-" + $routeParams.id;
            var cookie = ($cookieStore.get(cookieKey)) ? JSON.parse($cookieStore.get(cookieKey)) : {};
            console.log(cookie);
            if (cookie.currentIterationCount == data[0]) {
                console.log("user has cookie!");
                $window.location.hash = "/voted/" + $routeParams.id;
            }

            // TODO: this is a minor race condition
            $scope.vote = function(ballot) {
                var cookieId = Math.random();
                var Ballot = $resource('/rest/moodlies/:moodlyId/ballots', {moodlyId:$routeParams.id},{});

                CookieService.put($routeParams.id, {cookieId:cookieId, ballot:ballot, currentIterationCount:parseInt(data[0], 10)});

                Ballot.save({cookieId:cookieId.toString(), vote:ballot});
                console.log(ballot);
                $window.location.hash = "/voted/" + $routeParams.id;
            };
        });

    }

    function VotedCtrl($scope, $routeParams, $resource, CookieService) {
        $scope.ballot = CookieService.get($routeParams.id).ballot;

        var Ballot = $resource('/rest/moodlies/:moodlyId/ballots', {moodlyId:$routeParams.id},
                {
                    get : {
                      method: 'GET',
                      isArray: true
                    }
                }
        );

        Ballot.get(function(data) {
            var cIC = CookieService.get($routeParams.id).currentIterationCount;
            console.log(data);
            $scope.avg = Math.round((data.reduce(function(acc, b) {
                return acc + (b.iterationCount == cIC ? b.vote : 0)
            }, 0) / data.length) * 100) / 100;
        });
    }

    angular.module('moodly', ['ngRoute', 'ngResource', 'ngCookies' ]);

    angular.module('moodly').service('CookieService', function($cookieStore) {
        this.get = function(cookieName) {
            var cookieKey = "moodly-" + cookieName;
            return ($cookieStore.get(cookieKey)) ? JSON.parse($cookieStore.get(cookieKey)) : {};
        };

        this.put = function(cookieName, data) {
            var cookieKey = "moodly-" + cookieName;
            $cookieStore.put(cookieKey, JSON.stringify(data));
        };
    });

    angular.module('moodly').config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/', {
            templateUrl: 'partials/moodlies.html',
            controller: MoodlyCtrl,
            reloadOnSearch: false
        }).when('/voting/:id', {
            templateUrl: 'partials/voting.html',
            controller: VotingCtrl,
            reloadOnSearch: false
        }).when('/voted/:id', {
            templateUrl: 'partials/voted.html',
            controller: VotedCtrl,
            reloadOnSearch: false
        }).otherwise({redirectTo: '/'});
    }]);

    angular.bootstrap(document, ['moodly']);


});

