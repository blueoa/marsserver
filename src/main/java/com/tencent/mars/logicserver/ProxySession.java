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

package com.tencent.mars.logicserver;

import com.tencent.mars.datacenter.CacheData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zhaoyuan on 16/2/2.
 */
public class ProxySession {

    public static class Manager {

        private static Map<String, ChannelHandlerContext> channels = new HashMap<>();
        private static Map<String, PreparedStatement> cachedStatement = new HashMap<>();

        public static boolean connect() {
            return false;
        }

        public static void disconnect() {
        }

        public static ProxySession get(ChannelHandlerContext ctx) {
            final String name = ctx.name();

            return null;
        }

        public static ChannelHandlerContext getChannelContextByUin(final int uin) {

            return null;
        }

        public static ChannelHandlerContext getChannelContextByClientAddr(String clientAddr) {
            try{
                return channels.get(clientAddr);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        public static void updateChannelsByClientAddr(String clientAddr, ChannelHandlerContext context) {
            try {
                channels.put(clientAddr, context);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void updateByCTX(ProxySession session, ChannelHandlerContext ctx) {
        }
    }

    public int uin;
    public String key;
}
