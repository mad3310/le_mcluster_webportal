$(function(){
	//隐藏搜索框
	$('#nav-search').addClass("hidden");
	queryContainer();
})
function queryContainer(){
	$("#tby tr").remove();
	getLoading();

	$.ajax({ 
		cache:false,
		type : "get",
		url:"/es/"+$("#clusterId").val()+"/containers",
		dataType : "json", 
		success : function(data) {
			removeLoading();
			if(error(data)) return;
			var array = data.data;
					
			var array_len=array.length;
			var tby = $("#tby");
 			var recordsArray=[];
			for (var i = 0, len =array_len; i < len; i++) {
				var tempObj=array[i];
				var containerId="<input name='container_id' value='"+tempObj.id+"' type='hidden' />";
				var containerName="<td>"+tempObj.containerName+"</td>";
				var hostIp="<td class='hidden-480'>"+tempObj.hostIp+"</td>";
				var ipAddr="<td>"+tempObj.ipAddr+"</td>";
				if(tempObj.mountDir != null){
					jsonStr = tempObj.mountDir.substring(1,tempObj.mountDir.length-1);
					jsonArr = jsonStr.split(",");
					var mountDirStr = "";
					var jsonArrLen=jsonArr.length;
					for (var j = 0; j < jsonArrLen; j++){						
						mountDirStr += jsonArr[j]+"<br/>";					
					}
					var mountDir="<td class='hidden-480'>"+mountDirStr+"</td>";
				}else{
					var mountDir="<td class='hidden-480'>-</td>";
				}
				if(tempObj.zookeeperIp != null){
					var zookeeperIp="<td class='hidden-480'>"+tempObj.zookeeperIp+"</td>";
				}else{
					var zookeeperIp="<td class='hidden-480'>-</td>";
				}

				var status="<td>"+esStateTransform(tempObj.status)+"</td>";

				var option = "<td></td>"			
				
				recordsArray.push("<tr>",containerId,containerName,hostIp,ipAddr,mountDir,zookeeperIp,status,option,"</tr>");
			}
			tby.append(recordsArray.join(''));
			/*初始化tooltip*/
			$('[data-toggle = "tooltip"]').tooltip();
		}
	});
}

