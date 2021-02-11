package com.nettystudy.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TankMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int i = in.readableBytes();
        if(i<8) return;//解决拆包问题。8字节代表2个整数

        int x = in.readInt();
        int y = in.readInt();

        out.add(new TankMsg(x,y));
    }
}
