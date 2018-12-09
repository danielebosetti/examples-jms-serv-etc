/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package netty.echo;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
      System.out.println("channelRegistered: ctx="+ctx);
      super.channelRegistered(ctx);
    }  
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
      System.out.println("channelUnregistered: ctx="+ctx);
    super.channelUnregistered(ctx);
    }
    
    int c=0;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
      System.out.println("received msg=["+msg+"] type="+msg.getClass().getSimpleName() + " ctx="+ctx);
        //ctx.write("c="+c++ + " " + msg);
      ctx.write("srv>:::"+ msg + ":::\n");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
