package com.tencent.mars.datacenter;

import com.hazelcast.core.*;

public class CacheMembershipListener implements MembershipListener {

    private HazelcastInstance hazelcastInstance;

    public CacheMembershipListener(HazelcastInstance hazelcastInstance){
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void memberAdded(MembershipEvent membershipEvent) {
        for (Partition partition : hazelcastInstance.getPartitionService().getPartitions()) {
            if(membershipEvent.getMember().equals(partition.getOwner())){

            }
        }
    }

    @Override
    public void memberRemoved(MembershipEvent membershipEvent) {

    }

    @Override
    public void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {

    }
}
