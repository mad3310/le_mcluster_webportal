/**
 * Created by jiangfei on 2015/8/12.
 */
define(['controllers/app.controller'], function (controllerModule) {

  controllerModule.controller('VpcCreateModalCtrl', function (Config, HttpService,WidgetService,Utility, ModelService,CurrentContext, $scope, $modalInstance,$timeout,$window, region) {

    $scope.vpcCreate = {
      name:'',
      isCreateSubnet:'false',
      subnet:{
        name:''
      }
    };

    $scope.closeModal=function(){
      $modalInstance.dismiss('cancel');
    };
    $scope.createVpc = function () {
      if (!$scope.vpc_create_form.$valid) return;
      var vpcCreateUrl = '';
      if($scope.vpcCreate.isCreateSubnet === 'false'){
        vpcCreateUrl = Config.urls.vpc_create;
        var data = {
          region:region,
          name: $scope.vpcCreate.name
        };
      }else{
        vpcCreateUrl = Config.urls.vpc_subnet_create;
        var data = {
          region:region,
          networkName:$scope.vpcCreate.name,
          subnetName:$scope.vpcCreate.subnet.name,
          cidr:$scope.vpcCreate.subnet.selectedCidr.value,
          autoGatewayIp:false,
          gatewayIp:$scope.vpcCreate.subnet.selectedCidr.relatedOption.gatewayIp
        }
      }

      $scope.isFormSubmiting=true;
      HttpService.doPost(vpcCreateUrl, data).success(function (data, status, headers, config) {
        if(data.result===1){
          $modalInstance.close({result:1});
          WidgetService.notifySuccess(data.msgs[0]||'创建VPC完成');
        }
        else{
          $scope.isFormSubmiting=false;
          WidgetService.notifyError(data.msgs[0]||'创建VPC失败');
        }
      });
    };
    var initComponents = function () {
          initSelector();
        },
        initSelector = function () {
          $scope.vpcCreate.subnet.cidrs = [
            {
              cidr: '192.168.1.0/24',
              gatewayIp: '192.168.1.1'
            },
            {
              cidr: '192.168.0.0/24',
              gatewayIp: '192.168.0.1'
            }
          ];
          $scope.vpcCreate.subnet.cidrListSelectorData = $scope.vpcCreate.subnet.cidrs.map(function (cidr) {
            return new ModelService.SelectModel(cidr.cidr, cidr.cidr,{gatewayIp: cidr.gatewayIp});
          });
          $scope.vpcCreate.subnet.selectedCidr = $scope.vpcCreate.subnet.cidrListSelectorData[0];
        };

    initComponents();
  });

});
