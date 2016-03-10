package com.letv.portal.task.rds.service.add.impl;

import com.letv.common.exception.ValidateException;
import com.letv.common.result.ApiResultObject;
import com.letv.portal.constant.Constant;
import com.letv.portal.enumeration.MclusterStatus;
import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.MclusterModel;
import com.letv.portal.model.common.ZookeeperInfo;
import com.letv.portal.model.task.TaskResult;
import com.letv.portal.model.task.service.IBaseTaskService;
import com.letv.portal.python.service.IPythonService;
import com.letv.portal.service.IContainerService;
import com.letv.portal.service.IMclusterService;
import com.letv.portal.task.rds.service.impl.BaseTask4RDSServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("taskMclusterAddInitZookeeperService")
public class TaskMclusterInitZookeeperServiceImpl extends BaseTask4RDSServiceImpl implements IBaseTaskService{

	@Autowired
	private IPythonService pythonService;
	@Autowired
	private IContainerService containerService;
	@Autowired
	private IMclusterService mclusterService;
	
	private final static Logger logger = LoggerFactory.getLogger(TaskMclusterInitZookeeperServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;

		Long mclusterId = getLongFromObject(params.get("mclusterId"));
		if(mclusterId == null)
			throw new ValidateException("params's mclusterId is null");
		
		//执行业务
		MclusterModel mclusterModel = this.mclusterService.selectById(mclusterId);
		if(mclusterModel == null)
			throw new ValidateException("mclusterModel is null by mclusterId:" + mclusterId);

		//执行业务
		String namesstr = (String)params.get("addNames");
		String[] addNames = namesstr.split(",");
		List<ContainerModel> containers = new ArrayList<ContainerModel>();
		for (String addName:addNames) {
			containers.add(this.containerService.selectByName(addName));
		}

		if(containers.isEmpty())
			throw new ValidateException("containers is empty by name:" + namesstr);

		//get zk ip from old container.
		List<ContainerModel> oldContainers = this.containerService.selectByMclusterId(mclusterId);
		if(oldContainers.isEmpty())
			throw new ValidateException("old containers is empty by mclusterId:" + mclusterId);
		String zookeeperIp = oldContainers.get(0).getZookeeperIp();
		if(StringUtils.isEmpty(zookeeperIp))
			throw new ValidateException("init zk error,old container's zk ip is null");

		for (int i = 0; i < containers.size(); i++) {
			ContainerModel container = containers.get(i);
			String nodeIp = container.getIpAddr();
			Map<String, String> zkParm = new HashMap<String,String>();
			zkParm.put("zkAddress", zookeeperIp);
			zkParm.put("zkPort", Constant.ZK_PORT);
			ApiResultObject resultObject = this.pythonService.initZookeeper(nodeIp,zkParm);
			
			tr = analyzeRestServiceResult(resultObject);
			if(!tr.isSuccess()) {
				tr.setResult("the" + (i+1) +"node error:" + tr.getResult());
				break;
			} else {
				container.setZookeeperIp(zookeeperIp);
				this.containerService.updateBySelective(container);
			}
		}
		
		tr.setParams(params);
		return tr;
	}
	@Override
	public void callBack(TaskResult tr) {
//		super.callBack(tr);
	}

	@Override
	public void rollBack(TaskResult tr) {
		String namesstr  =  (String) ((Map<String, Object>) tr.getParams()).get("addNames");
		String[] addNames = namesstr.split(",");
		for (String addName:addNames) {
			ContainerModel containerModel = this.containerService.selectByName(addName);
			if(MclusterStatus.ADDING.getValue() == containerModel.getStatus()) {
				containerModel.setStatus(MclusterStatus.ADDINGFAILED.getValue());
				this.containerService.updateBySelective(containerModel);
			}
		}
		Long mclusterId = getLongFromObject(((Map<String, Object>) tr.getParams()).get("mclusterId"));
		MclusterModel mcluster = this.mclusterService.selectById(mclusterId);
		mcluster.setStatus(MclusterStatus.ADDINGFAILED.getValue());
		this.mclusterService.updateBySelective(mcluster);
//		super.rollBack(tr);
	}
	
}