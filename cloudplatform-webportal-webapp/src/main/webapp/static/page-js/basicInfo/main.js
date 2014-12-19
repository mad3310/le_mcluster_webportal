/**
 * Created by yaokuo on 2014/12/12.
 */
define(function(require){
	var $ = require('jquery');
    var common = require('../common');
    var cn = new common();

/*初始化工具提示*/
    cn.Tooltip('#serviceName');

/*初始化侧边栏菜单*/
    var index = [0,0];
    cn.Sidebar(index);//index为菜单中的排序(1-12)
    
/*手风琴收放效果箭头变化*/
    cn.Collapse(".collapse-selector");

/*加载数据*/
    var dataHandler = require('./dataHandler');
    var dbInfoHandler = new dataHandler();

    cn.GetData("/db/"+$("#dbId").val(),dbInfoHandler.DbInfoHandler);
});
