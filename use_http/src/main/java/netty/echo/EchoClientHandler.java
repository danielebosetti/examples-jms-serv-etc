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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf messageBuff;
    private ChannelHandlerContext ctx;

    /**
     * Creates a client-side handler.
     */
    public EchoClientHandler() {
      
      System.out.println("EchoClientHandler: created");
      
        new Thread() {
          public void run() {
            
            BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
            
            String line=null;
            do {
              try {
                line=buffer.readLine()+"\n";
                System.out.println("read from console: "+line);
                messageBuff = Unpooled.buffer(EchoClient.SIZE);
                messageBuff.writeBytes(line.getBytes());
                
                if (ctx!=null) {
                  System.out.println("sending to server");
                  ctx.write(messageBuff);
                  ctx.flush();
                }
                else {
                  System.out.println("ctx is null! skip sending");
                }
                
              } catch (IOException e) {
                e.printStackTrace(System.out);
              }
            }
            while (line!=null);
          };
        }.start();
        System.out.println("console thread created, type something "
            + "then press enter to send the message to the server");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
      this.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
      System.out.println("channelRead msg="+msg);
      if (msg instanceof ByteBuf) {
        ByteBuf bbuf = (ByteBuf) msg;
        String string = bbuf.toString(Charset.forName("UTF-8"));
        System.out.println("bbuf content=["+ string +"]");
      }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
