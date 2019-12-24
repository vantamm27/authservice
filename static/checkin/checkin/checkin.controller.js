(function () {
    'use strict';


    theApp.service('hexafy', function() {
        this.myFunc = function (x) {
            return x.toString(16);
        }
    });
    theApp.controller('CheckinController', CheckinController);
    //CheckinController.$inject = ['$scope', '$rootScope', '$interval', '$http', '$q', 'hexafy'];
    function CheckinController($scope, $rootScope, $interval, $http, CheckinService, hexafy) {
        $scope.updatettmplTime = Date.now(); // update template trong ng-include
        $scope.records = []
        $scope.initData = function () {
          // reload data after 1s
          $interval($scope.reloadData, 1*1000);
          //$scope.reloadData()
        }
        console.log(hexafy.myFunc(222));

        $scope.reloadData = function (){
          console.log(CheckinService.getRecords());
          CheckinService.getRecords().then(function (data) {
                console.log("CheckinService.getRecord()", data);
                if (data.err == 0) {
                    $scope.records = data.dt;
                } else {
                    console.log("CheckinService getRecord", data);
                }
            })

          // $scope.records =[
          //   {name:"Võ Văn Tâm", time:"2019-12-12 10:00:00", status:"OK"},
          //   {name:"Võ Văn Tâm2", time:"2019-12-12 10:01:00",status:"OK"}] ;

            console.log(Date.now())
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
