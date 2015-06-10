package com.letv.portal.service.openstack.resource.manager;

import java.util.List;

import com.letv.portal.service.openstack.exception.RegionNotFoundException;
import com.letv.portal.service.openstack.exception.ResourceNotFoundException;
import com.letv.portal.service.openstack.resource.ImageResource;

public interface ImageManager extends ResourceManager {
	List<ImageResource> list(String region) throws RegionNotFoundException;

	ImageResource get(String region, String id) throws RegionNotFoundException,
			ResourceNotFoundException;
}
