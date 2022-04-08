/*
* Tencent is pleased to support the open source community by making Mars available.
* Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
*
* Licensed under the MIT License (the "License"); you may not use this file except in 
* compliance with the License. You may obtain a copy of the License at
* http://opensource.org/licenses/MIT
*
* Unless required by applicable law or agreed to in writing, software distributed under the License is
* distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
* either express or implied. See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.tencent.mars.datacenter;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;
import com.tencent.mars.logicserver.ProxySession;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by zhaoyuan on 16/2/2.
 */
public class CacheData {

    public static Logger logger = Logger.getLogger(CacheData.class.getName());

    private static HazelcastInstance hazelcastInstance;

    public static MultiMap<String, ProxySession> prepareStatement(String sql) throws SQLException {
        return hazelcastInstance.getMultiMap("userSession");
    }

    public static void connect() {
        try {
            hazelcastInstance = Hazelcast.newHazelcastInstance();
            logger.info("opened database successfully");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void disconnect() {
        if (hazelcastInstance != null) {
            hazelcastInstance.shutdown();
        }
    }
}
