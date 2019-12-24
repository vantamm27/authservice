(function () {
    'use strict';

    theApp.controller('FingerController', FingerController);
    //CheckinController.$inject = ['$scope', '$rootScope', '$interval', '$http', '$q', 'hexafy'];
    function FingerController($scope, $rootScope, $interval, $http, FingerService) {
        $scope.updatettmplTime = Date.now(); // update template trong ng-include
        $scope.fingers = [];
        $scope.newFinger = {}
        $scope.initData = function () {
          // reload data after 1s
          // $interval($scope.reloadData, 1*1000);
          //$scope.reloadData()
          $scope.getAllFinger();
        }

        $scope.getAllFinger = function (){
          console.log("FingerService.getAllFinger ");
          FingerService.getAllFinger().then(function (data) {
                console.log("FingerService.getAllFinger()", data);
                if (data.err == 0) {
                    $scope.fingers = data.dt;
                } else {
                    console.log("CheckinService getRecord", data);
                }
            })

          // $scope.records =[
          //   {name:"Võ Văn Tâm", time:"2019-12-12 10:00:00", status:"OK"},
          //   {name:"Võ Văn Tâm2", time:"2019-12-12 10:01:00",status:"OK"}] ;

            console.log(Date.now())
        }

        $scope.registerFinger = function (){
          console.log("FingerService.registerFinger ");
          $scope.newFinger
          var dt = {name:$scope.newFinger.name, code: $scope.newFinger.code}
          FingerService.registerFinger(dt).then(function (data) {
                console.log("FingerService.registerFinger()", data);
                if (data.err == 0) {
                    alert("Đăng ký thành công.");
                    $scope.getAllFinger();
                } else {
                    alert("Đăng ký không thành công. Vui lòng thử  lại");
                    console.log("FingerService.registerFinger", data);
                }
            })
        }

        /*
         $scope.currentDay = function () {
         var today = new Date();
         $scope.fromDate = today;
         $(".fromdate").datepicker().datepicker('update', $scope.fromDate);
         $scope.changeDatefrom();
         }
         */




        $scope.initData();


    }

}
)();
