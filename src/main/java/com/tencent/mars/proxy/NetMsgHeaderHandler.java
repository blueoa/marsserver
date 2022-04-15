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

package com.tencent.mars.proxy;

import com.tencent.mars.proto.Main;
import com.tencent.mars.utils.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoyuan on 16/2/2.
 */
public class NetMsgHeaderHandler extends ChannelInboundHandlerAdapter {

    public static Logger logger = Logger.getLogger(NetMsgHeaderHandler.class.getName());

    OkHttpClient httpClient = new OkHttpClient();

    private static Map<Integer, String> CMD_PATH_MAP = new HashMap<>();

    static {
        CMD_PATH_MAP.put(Main.CmdID.CMD_ID_HELLO_VALUE, "mars/hello");
        CMD_PATH_MAP.put(Main.CmdID.CMD_ID_SEND_MESSAGE_VALUE, "/mars/sendmessage");
        CMD_PATH_MAP.put(Main.CmdID.CMD_ID_HELLO2_VALUE, "/mars/hello2");
    }

    public NetMsgHeaderHandler() {
        super();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info("client connected! " + ctx.toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            // decode request
            final NetMsgHeader msgXp = new NetMsgHeader();
            final InputStream socketInput = new ByteBufInputStream((ByteBuf) msg);
            boolean ret = msgXp.decode(socketInput);
            IOUtils.closeQuietly(socketInput);

            if (!ret) return;
            String webCgi = CMD_PATH_MAP.get(msgXp.cmdId);
            InputStream inputStream = doHttpRequest(webCgi, msgXp.body);
            if (inputStream != null) {
                msgXp.body = IOUtils.toByteArray(inputStream);
                byte[] respBuf = msgXp.encode();
                logger.info(LogUtils.format( "client resp, cmdId=%d, seq=%d, resp.len=%d", msgXp.cmdId, msgXp.seq, msgXp.body == null ? 0 : msgXp.body.length));
                ctx.writeAndFlush(ctx.alloc().buffer().writeBytes(respBuf));
            }
            else {

            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * redirect request to webserver
     *
     * @param path
     * @param data
     * @return
     */
    private InputStream doHttpRequest(String path, byte[] data) throws IOException {
        RequestBody body = RequestBody.create(MediaType.get("application/octet-stream;charset=utf-8"), data);
        Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if(response.isSuccessful() && response.body() != null){
                return response.body().byteStream();
            }
        }
        return null;
    }

}

