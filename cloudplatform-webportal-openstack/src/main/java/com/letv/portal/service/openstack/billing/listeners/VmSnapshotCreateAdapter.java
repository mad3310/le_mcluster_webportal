package com.letv.portal.service.openstack.billing.listeners;

import com.letv.portal.service.openstack.billing.listeners.event.VmSnapshotCreateEvent;

/**
 * Created by zhouxianguang on 2015/10/19.
 */
public class VmSnapshotCreateAdapter implements VmSnapshotCreateListener {
    @Override
    public void vmSnapshotCreated(VmSnapshotCreateEvent event) throws Exception {}
}
