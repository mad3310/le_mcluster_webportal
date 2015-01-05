/**
 * Created by yaokuo on 2014/12/12.
 */
define(function(require){
    var common = require('../common');
    var cn = new common();
    var $ = require("jquery");
    require("bootstrapValidator")($);
    
    /*初始化标签页*/
	$('#setab a').click(function (e) {
		e.preventDefault()
		$(this).tab('show')
	})
   $("#sqlInject-tab").click(function() {
      $("#refresh").hide();
	}); 
   $("#whitelist-tab").click(function() {
      $("#refresh").show();
	});

    /*初始化按钮 --begin*/
    $("#modifyIpList").click(function () {
        $("#ipList").addClass("hide");
        $("#ipForm").removeClass("hide");
    })
    $("[name = 'submitIpForm']").click(function () {
        $("#ipForm").addClass("hide");
        $("#ipList").removeClass("hide");
        var dbId = $("#dbId").val();
        var ips = $("#iplist-textarea").val();
        cn.PostData(
            "/dbIp",
            {
                dbId:dbId,
                ips:ips
            },
            asyncModifyIpData()
        );

    })
    $("[name = 'cancleIpForm']").click(function () {
        $("#ipForm").addClass("hide");
        $("#ipList").removeClass("hide");
    })
    $("#refresh").click(function() {
        asyncModifyIpData();
    });
   /*初始化按钮 --end*/

    $('#ipForm').bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            'iplist-textarea': {
                validators: {
                    notEmpty: {
                        message:'密码不能为空'
                    },regexp: {
                        regexp: /^(((\d|\d\d|1\d\d|2[0-4]\d|25[0-5])((\.(\d|\d\d|1\d\d|2[0-4]\d|25[0-5]))|(\.\%)){3}\,)*)((\d|\d\d|1\d\d|2[0-4]\d|25[0-5])((\.(\d|\d\d|1\d\d|2[0-4]\d|25[0-5]))|(\.\%)){3})$/,
                        message: '请按提示格式输入'
                    }
                }
            }
        }
    }).on('error.field.bv', function(e, data) {
        $("[name = 'submitIpForm']").addClass("disabled");
    }).on('success.field.bv', function(e, data) {
        $("[name = 'submitIpForm']").removeClass("disabled");
    });
    /*加载数据*/
    var dataHandler = require('./dataHandler');
    var securityDataHandler = new dataHandler();

    asyncModifyIpData();
    function asyncModifyIpData(){
        window.setTimeout(function () {
            cn.GetData("/dbIp/"+$("#dbId").val(),securityDataHandler.DbUserIpListHandler);   //获取IP列表信息
        },500);
    }
});