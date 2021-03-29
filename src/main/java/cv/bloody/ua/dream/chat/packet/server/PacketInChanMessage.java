package cv.bloody.ua.dream.chat.packet.server;

import cv.bloody.ua.dream.chat.ServerApp;
import cv.bloody.ua.dream.chat.chat.ChatColor;
import cv.bloody.ua.dream.chat.chat.Message;
import cv.bloody.ua.dream.chat.handlers.MainHandler;
import cv.bloody.ua.dream.chat.packet.client.PacketOutChatMessage;
import cv.bloody.ua.dream.chat.packet.client.PacketOutPrivateMessage;
import cv.bloody.ua.dream.chat.users.ServerMessage;
import cv.bloody.ua.dream.chat.users.User;
import cv.bloody.ua.dream.chat.users.UserGroup;
import cv.bloody.ua.dream.chat.users.UsersManager;
import io.netty.channel.ChannelHandlerContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringJoiner;

public class PacketInChanMessage extends ServerPacket {

    private final String message;
    private final ChannelHandlerContext context;

    public PacketInChanMessage(ChannelHandlerContext context , String message , int id) {
        super(id);
        this.message = message;
        this.context = context;
    }

    @Override
    public void handle() throws Exception {
    }

    @Override
    public void handle(User user) throws Exception {
        if(message.length() == 0) {
            context.writeAndFlush(new PacketOutChatMessage(new ServerMessage("message is empty")).toJSON().toString());
            return;
        }
        if(message.charAt(0) == '/') {
            String[] args = message.split(" ");
            if(args[0].toLowerCase().contains("/help")) {
                context.writeAndFlush(new PacketOutChatMessage(new ServerMessage("/help - show commands")).toJSON().toString());
                context.writeAndFlush(new PacketOutChatMessage(new ServerMessage("/m {user} {message}- send private message")).toJSON().toString());
                return;
            }
            if(args[0].toLowerCase().contains("/m")) {
                if(args.length < 3) {
                    context.writeAndFlush(new PacketOutChatMessage(new ServerMessage("/m {user} {message}- send private message")).toJSON().toString());
                }else {
                    User u = UsersManager.getInstance().getUser(args[1].toLowerCase());
                    if(u == null) {
                        context.writeAndFlush(new PacketOutChatMessage(new ServerMessage(ChatColor.ANSI_RED + args[1] + " is offline")).toJSON().toString());
                    }else {
                        StringJoiner joiner = new StringJoiner(" ");
                        for(int i = 2 ; i < args.length ; i++) joiner.add(args[i]);
                        u.sendPacket(new PacketOutPrivateMessage(new CraftMessage(user , joiner.toString())));
                    }
                }
                return;
            }
            context.writeAndFlush(new PacketOutChatMessage(new ServerMessage("write /help to get command")).toJSON().toString());
            return;
        }
        UsersManager.getInstance().sendAll(new PacketOutChatMessage(new CraftMessage(user , message)));
    }

    private static class CraftMessage implements Message {

        private final String sender;
        private final String displayName;
        private final UserGroup userGroup;
        private final String message;

        public CraftMessage(User user , String message) {
            sender = user.getLogin();
            displayName = user.getDisplayName();
            userGroup = user.getGroup();
            this.message = message;
        }

        @Override
        public String getSender() {
            return sender;
        }

        @Override
        public String getDisplayName() {
            return displayName;
        }

        @Override
        public UserGroup getUserGroup() {
            return userGroup;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public long getDate() {
            return System.currentTimeMillis();
        }
    }
}
