package com.letv.portal.proxy.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.MonitorIndexModel;
import com.letv.portal.proxy.IMonitorProxy;
import com.letv.portal.python.service.IBuildTaskService;
import com.letv.portal.service.IContainerService;
import com.letv.portal.service.IMonitorIndexService;
import com.letv.portal.service.IMonitorService;

@Component("monitorProxy")
public class MonitorProxyImpl implements IMonitorProxy{
	private final static Logger logger = LoggerFactory.getLogger(MonitorProxyImpl.class);
	@Autowired
	private IMonitorService monitorService;
	
	@Autowired
	private IBuildTaskService buildTaskService;
	
	@Autowired
	private IContainerService containerService;
	
	@Autowired
	private IMonitorIndexService monitorIndexService;
	
	@Override
	public void collectMclusterServiceData() {
		Map<String,String> map = new  HashMap<String,String>();
		map.put("type", "mclusternode");
		List<ContainerModel> contianers = this.containerService.selectByMap(map);
		
		Map<String,Object> indexParams = new  HashMap<String,Object>();
		indexParams.put("status", 1);
		List<MonitorIndexModel> indexs = this.monitorIndexService.selectByMap(indexParams);
		Date data = new Date();
		logger.info("collectMclusterServiceData start");
		for (ContainerModel container : contianers) {
			for (MonitorIndexModel index : indexs) {
				this.buildTaskService.getContainerServiceData(container, index,data);
			}
		}
		logger.info("collectMclusterServiceData end");
	}

}