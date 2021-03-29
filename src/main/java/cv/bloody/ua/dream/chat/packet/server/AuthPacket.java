package cv.bloody.ua.dream.chat.packet.server;

import cv.bloody.ua.dream.chat.ServerApp;
import cv.bloody.ua.dream.chat.users.User;
import cv.bloody.ua.dream.chat.users.UserGroup;
import cv.bloody.ua.dream.chat.users.UsersManager;
import io.netty.channel.ChannelHandlerContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthPacket extends ServerPacket {

    private final ChannelHandlerContext context;
    private final String login;
    private final String password;

    private User user;

    public AuthPacket(ChannelHandlerContext context , String login , String password , int id) {
        super(id);
        this.login = login.toLowerCase();
        this.context = context;
        this.password = password;
    }

    @Override
    public void handle() throws Exception {
        if(UsersManager.getInstance().getUser(login) != null) throw new IllegalStateException("logined");
        PreparedStatement statement = ServerApp.getPool().getSelectByName();
        statement.setString(1 , login);
        ResultSet set = statement.executeQuery();
        if(!set.next()) {
            set.close();
            throw new IllegalStateException();
        }
        String password = set.getString("password");
        String displayName = set.getString("display_name");
        String userGroup = set.getString("user_group");
        set.close();
        if(!password.equals(RegistrationPacket.decodeToSha256(this.password))) throw new IllegalArgumentException();
        statement = ServerApp.getPool().getUpdateLastLogin();
        statement.setLong(1 , System.currentTimeMillis());
        statement.setString(2 , login);
        UsersManager.getInstance().authorizeUser(context , login , displayName , UserGroup.valueOf(userGroup.toUpperCase()));
        user = UsersManager.getInstance().getUser(login);
    }

    public User getUser() {
        return user;
    }
}
