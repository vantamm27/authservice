(function () {
    'use strict';
    theApp.service('FingerService', FingerService);

    FingerService.$inject = ['$rootScope', '$http', '$q', 'API_URL'];
    function FingerService($rootScope, $http, $q, API_URL) {
        var today = new Date();
        var d = today.getDate();
        var m = today.getMonth();
        var y = today.getFullYear();
        var service = {};
        //service.getRecords = getRecords;
        service.getAllFinger = getAllFinger;
        service.registerFinger = registerFinger;

        //service.createEvent = createEvent;
        service.getErrMsg = getErrMsg;
        service.getcurrtime = getcurrtime;

        return service;

        function getAllFinger() {
            var cm = "get";
            var url = API_URL +"register?cm=" + cm;
            console.log(url);

            return $http.get(url).then(function (res) {
                console.log("getRecords", res);
                return res.data
            }, handleError('Error get all record'));
        }
        function registerFinger(dt) {
            var cm = "finger";
            var url = API_URL +"register?cm=" + cm +"&name=" + dt.name +"&code="+dt.code;
            console.log(url);

            return $http.get(url).then(function (res) {
                console.log("registerFinger", res);
                return res.data
            }, handleError('Error get all record'));
        }



        function  getErrMsg(data) {
            var msg = GlobalSerivce.getErrMsg(data.err);
            msg += parserMsgErr(data);
            return msg;
        }

        function parserMsgErr(data) {

            var result = "";
            if (result.length > 0) {
                result = ": " + result;
            }
            return result;
        }

        function  getcurrtime() {
            var cm = "getcurrtime";
            var url = API_URL + "util?cm=" + cm;

            return $http.get(url).then(handleSuccess, handleError('Error get currtime'));
        }

        function handleSuccess(res, url) {
            if (url == undefined) {
                return res.data;
            }

            console.log("handle sucess", url, res);
            //$rootScope.caches

            var cache = $rootScope.caches.get(url);
            console.log(cache);
            return $q.when(cache.data);
        }

        function handleError(error) {
            return function () {
                return {err: -2, msg: error};
            };
        }
    }

}
)();
