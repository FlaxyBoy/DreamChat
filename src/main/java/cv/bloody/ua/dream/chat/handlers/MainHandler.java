package cv.bloody.ua.dream.chat.handlers;


import cv.bloody.ua.dream.chat.DreamLogger;
import cv.bloody.ua.dream.chat.ServerApp;
import cv.bloody.ua.dream.chat.packet.server.ServerPacket;
import cv.bloody.ua.dream.chat.users.User;
import cv.bloody.ua.dream.chat.users.UsersManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private final User user;

    public MainHandler(User user) {
        this.user = user;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ServerPacket packet = NetworkManager.decode((String) msg, ctx);
            if(packet.getID() != 1 && packet.getID() != 2) {
                packet.handle(user);
            }
        }catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ServerApp.logger.info(ctx.channel().remoteAddress().toString() + " |" + user.getLogin() + " is disconnected");
        UsersManager.getInstance().inactivate(user);
        super.channelInactive(ctx);
    }
}
