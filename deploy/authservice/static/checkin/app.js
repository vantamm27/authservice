var theApp = angular.module("theApp", ["ngRoute"]);

theApp.constant('API_URL', window.location.protocol + '//' + window.location.host + '/api/v1/'); //define CONST API_URL

theApp.config(function($routeProvider) {
  $routeProvider
  .when("/", {
    controller: 'CheckinController',
    templateUrl : "checkin/checkin.view.html"
  })
  .otherwise({redirectTo: '/'});
//sdssd
});
