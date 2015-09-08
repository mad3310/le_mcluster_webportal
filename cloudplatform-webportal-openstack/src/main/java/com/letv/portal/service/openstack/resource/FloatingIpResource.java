package com.letv.portal.service.openstack.resource;

public interface FloatingIpResource extends Resource{
	String getRegionDisplayName();
	Integer getBandWidth();
	String getIpAddress();
	String getBindResourceType();
	Resource getBindResource(); 
	String getStatus();
}
