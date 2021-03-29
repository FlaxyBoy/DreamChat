package cv.bloody.ua.dream.chat.users;

import cv.bloody.ua.dream.chat.packet.client.ClientPacket;
import io.netty.channel.ChannelHandlerContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserProfile implements User {

    private final String login;
    private final String displayName;
    private UserGroup group;
    private final ChannelHandlerContext context;

    public UserProfile(ChannelHandlerContext context , String login , String displayName , UserGroup group) {
        this.context = context;
        this.login = login;
        this.displayName = displayName;
        this.group = group;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public UserGroup getGroup() {
        return UserGroup.USER;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public void sendPacket(ClientPacket packet) {
        context.channel().writeAndFlush(packet.toJSON().toString());
    }

    @Override
    public void leave() {

    }
}
