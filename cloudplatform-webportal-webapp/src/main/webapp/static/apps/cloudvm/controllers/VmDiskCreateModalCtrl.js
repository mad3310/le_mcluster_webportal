/**
 * Created by jiangfei on 2015/8/12.
 */
define(['controllers/app.controller'], function (controllerModule) {

  controllerModule.controller('VmDiskCreateModalCtrl', function (Config, HttpService,WidgetService,Utility,CurrentContext, $scope, $modalInstance,$timeout,$window, region) {

    Utility.getRzSliderHack($scope)();
    $scope.isOrderSubmiting=false;
    $scope.diskName = '';
    $scope.diskTypeList = [];
    $scope.selectedDiskType = null;
    $scope.diskVolume = 10;
    $scope.diskCount = 1;

    $scope.closeModal=function(){
      $modalInstance.dismiss('cancel');
    };
    $scope.isSelectedDiskType = function (diskType) {
      return $scope.selectedDiskType === diskType;
    };
    $scope.selectDiskType = function (diskType) {
      $scope.selectedDiskType = diskType;
    };
    $scope.createDisk = function () {
      if (!$scope.vm_disk_create_form.$valid) return;
      var data = {
        name: $scope.diskName,
        description:'',
        volumeTypeId:$scope.selectedDiskType.id,
        size: $scope.diskVolume,
        count:$scope.diskCount
      };
      $scope.isOrderSubmiting=true;
      HttpService.doPost(Config.urls.disk_create.replace('{region}',region), data).success(function (data, status, headers, config) {
        if(data.result===1){
          $modalInstance.close(data);
          WidgetService.notifySuccess('创建云硬盘成功');
        }
        else{
          WidgetService.notifyError(data.msgs[0]||'创建云硬盘失败');
          $scope.isOrderSubmiting=false;
        }
      });
    };

    var initComponents = function () {
        initDiskTypeSelector();
      },
      initDiskTypeSelector = function () {
        HttpService.doGet(Config.urls.vm_disk_type,{region:region}).success(function (data, status, headers, config) {
          $scope.diskTypeList=data.data;
          $scope.selectedDiskType = $scope.diskTypeList[0];
        });
      };

    initComponents();

  });

});
