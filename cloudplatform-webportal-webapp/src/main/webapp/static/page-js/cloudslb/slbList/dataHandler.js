/**
 * Created by yaokuo on 2014/12/14.
 * dblist page 
 */
define(function(require,exports,module){
    var $ = require('jquery');
    /*
	 * 引入相关js，使用分页组件
	 */
	require('bootstrap');
	require('paginator');
	
    var common = require('../../common');
    var cn = new common();
   
    
    var DataHandler = function(){
    };

    module.exports = DataHandler;

    DataHandler.prototype = {
        SlbListHandler : function(data){
        	$(".data-tr").remove();
            var $tby = $('#tby');
            var array = data.data.data;
            if(array.length == 0){
            	cn.emptyBlock($tby);
            	if($("#paginatorBlock").length > 0){
            		$("#paginatorBlock").hide();
            	}
            }else{
            	 if($("#noData").length > 0){
            		 $("#noData").remove();
            	 }
            	 if($("#paginatorBlock").length > 0){
            		 $("#paginatorBlock").show();
            	 }
                for(var i= 0, len= array.length;i<len;i++){
                    var td1 = $("<td width=\"10\">"
                            + "<input type=\"checkbox\" name=\"slbcluster_id\" value= \""+array[i].id+"\">"
                            + "</td>");
                    var slbName = "";
                    if(cn.Displayable(array[i].status)){
                        slbName = "<a href=\"/detail/slb/"+array[i].id+"\">" + array[i].slbName + "</a>"
                    }else{
                        slbName = "<span class=\"text-explode font-disabled\">"+array[i].slbName +"</span>"
                    }
                    var td2 = $("<td class=\"padding-left-32\">"
                            + slbName
                            +"</td>");
                    var td8 = $("<td class=\"padding-left-32\">"
                            + "<span>"+array[i].ip+"</span>"
                            +"</td>");
                    var td9=$("<td>-</td>");
                    if(array[i].slbConfigs != undefined || array[i].slbConfigs !=null){
                        td9 = $("<td>"
                            +addPort(array[i].slbConfigs)
                            +"</td>")
                    }
                    var td3 = $("<td>"
                        + "<span class=\"text-success\">已启用</span>"
                        + "</td>");
                    var td4="<td></td>";
                    if(array[i].hcluster != undefined && array[i].hcluster != null){
                        var td4 = $("<td>"
                        + "<span>"+array[i].hcluster.hclusterNameAlias+"</span></td>"
                        + "</td>");
                    }
                    var td5 = $("<td><span>"+cn.TranslateStatus(array[i].status)+"</span></td>");
                    var td6 = $("<td><span><span>包年  </span><span class=\"text-success\">"+cn.RemainAvailableTime(array[i].createTime)+"</span><span>天后到期</span></span></td>");
                    if(cn.Displayable(array[i].status)){
                    	var td7 = $("<td class=\"text-right\"><a href=\"/detail/slb/"+array[i].id+"\">管理</a><span class=\"text-explode font-disabled\">|续费|升级</span></td>");
                    }else{
                    	var td7 = $("<td class=\"text-right\"><span class=\"text-explode font-disabled\">管理|续费|升级</span></td>");
                    }
                    var tr = $("<tr class='data-tr'></tr>");
                    console.log(td9);
                    tr.append(td1).append(td2).append(td8).append(td9).append(td3).append(td4).append(td5).append(td6).append(td7);
                    tr.appendTo($tby);
                 }
            }
       
            /*
             * 设置分页数据
             */
            $("#totalRecords").html(data.data.totalRecords);
            $("#recordsPerPage").html(data.data.recordsPerPage);
            
            if(data.data.totalPages < 1){
        		data.data.totalPages = 1;
        	};
        	
            $('#paginator').bootstrapPaginator({
                currentPage: data.data.currentPage,
                totalPages:data.data.totalPages
            });
        }
    }
    function addPort(data){
        var ret="";
        for(var i= 0,len=data.length;i<len;i++){
            ret = ret
            +"<span class=\"text-success\">"+data[i].agentType+"</span>"
            +"<span class=\"text-success\">:</span>"
            +"<span class=\"text-success\">"+data[i].frontPort+"</span><br>"
        }
        return ret;
    }
});