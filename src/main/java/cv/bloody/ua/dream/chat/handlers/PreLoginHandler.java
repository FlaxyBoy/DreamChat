package cv.bloody.ua.dream.chat.handlers;

import cv.bloody.ua.dream.chat.ServerApp;
import cv.bloody.ua.dream.chat.packet.client.AuthorizeFailedPacket;
import cv.bloody.ua.dream.chat.packet.client.RegistrationFailedPacket;
import cv.bloody.ua.dream.chat.packet.server.AuthPacket;
import cv.bloody.ua.dream.chat.packet.server.RegistrationPacket;
import cv.bloody.ua.dream.chat.packet.server.ServerPacket;
import cv.bloody.ua.dream.chat.users.User;
import cv.bloody.ua.dream.chat.users.UsersManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PreLoginHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ServerApp.logger.info(ctx.channel().remoteAddress().toString() + " is connected");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ServerPacket packet;
            try {
                packet = NetworkManager.decode((String) msg, ctx);
            }catch (Exception e) {
                this.channelRead(ctx , msg);
                return;
            }
            if(packet.getID() == 1) {
                System.out.println("Encode packet with id 1");
                try {
                    packet.handle();
                    User user = ((AuthPacket )packet).getUser();
                    ctx.pipeline().removeLast();
                    ctx.pipeline().addLast(new MainHandler(user));
                }catch (IllegalArgumentException e) {
                    ctx.channel().writeAndFlush(new AuthorizeFailedPacket(AuthorizeFailedPacket.Reason.FAILED_PASSWORD).toJSON().toString());
                } catch (IllegalStateException e) {
                    if(e.getMessage().contains("logined")) {
                        ctx.channel().writeAndFlush(new AuthorizeFailedPacket(AuthorizeFailedPacket.Reason.USER_IS_LOGINED).toJSON().toString());
                        return;
                    }
                    ctx.channel().writeAndFlush(new AuthorizeFailedPacket(AuthorizeFailedPacket.Reason.UNKNOWN_USER).toJSON().toString());
                }catch (Exception e) {
                    ctx.channel().writeAndFlush(new AuthorizeFailedPacket(AuthorizeFailedPacket.Reason.SERVER_PROBLEM).toJSON().toString());
                }
            }
            if(packet.getID() == 2) {
                System.out.println("Encode packet with id 2");
                try {
                    packet.handle();
                    User user = ((RegistrationPacket)packet).getUser();
                    ctx.pipeline().removeLast();
                    ctx.pipeline().addLast(new MainHandler(user));
                }catch (IllegalArgumentException e) {
                    if(e.getMessage().contains("addres")) {
                        ctx.channel().writeAndFlush(new RegistrationFailedPacket(RegistrationFailedPacket.Reason.TO_MANY_ADDRES).toJSON().toString());
                        return;
                    }
                    ctx.channel().writeAndFlush(new RegistrationFailedPacket(RegistrationFailedPacket.Reason.WRONG_FORMAT_LOGIN).toJSON().toString());
                }catch (IllegalStateException e) {
                    ctx.channel().writeAndFlush(new RegistrationFailedPacket(RegistrationFailedPacket.Reason.LOGIN_IS_OCCUPIED).toJSON().toString());
                }catch (Exception e) {
                    ctx.channel().writeAndFlush(new RegistrationFailedPacket(RegistrationFailedPacket.Reason.SERVER_PROBLEM).toJSON().toString());
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ServerApp.logger.info(ctx.channel().localAddress().toString() + " is disconnected");
        super.channelInactive(ctx);
    }
}
