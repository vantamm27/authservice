var theApp = angular.module("theApp", ["ngRoute"]);

theApp.constant('API_URL', window.location.protocol + '//' + window.location.host + '/api/v1/'); //define CONST API_URL

theApp.config(function($routeProvider) {
  $routeProvider
  .when("/", {
    controller: 'FingerController',
    templateUrl : "finger/finger.view.html"
  })
  .when("/finger", {
    controller: 'FingerController',
    templateUrl : "finger/finger.view.html"
  })
  .otherwise({redirectTo: '/'});
//sdssd
});
