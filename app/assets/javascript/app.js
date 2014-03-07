'use strict';

// Declare here that angular is the US version - other locales can be easily substituted.

define('angular', ['webjars!angular.js', 'webjars!angular-route.js', 'webjars!angular-resource.js'], function () {
    return angular;
});

require([ 'angular' ], function (angular) {

});
