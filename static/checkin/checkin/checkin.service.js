(function () {
    'use strict';
    theApp.service('CheckinService', CheckinService);

    CheckinService.$inject = ['$rootScope', '$http', '$q', 'API_URL'];
    function CheckinService($rootScope, $http, $q, API_URL) {
        var today = new Date();
        var d = today.getDate();
        var m = today.getMonth();
        var y = today.getFullYear();
        var service = {};
        //service.getRecords = getRecords;
        service.getRecords = getRecords;

        //service.createEvent = createEvent;
        service.getErrMsg = getErrMsg;
        service.getcurrtime = getcurrtime;

        return service;

        function getRecords() {
            var cm = "log";
            var url = API_URL +"register?cm=" + cm;
            console.log(url);

            return $http.get(url).then(function (res) {
                console.log("getRecords", res);
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
