package cv.bloody.ua.dream.chat.users;

import com.sun.xml.internal.ws.api.message.Packet;
import cv.bloody.ua.dream.chat.ServerApp;
import cv.bloody.ua.dream.chat.chat.ChatColor;
import cv.bloody.ua.dream.chat.chat.Message;
import cv.bloody.ua.dream.chat.handlers.MainHandler;
import cv.bloody.ua.dream.chat.packet.client.AuthConfirmPacket;
import cv.bloody.ua.dream.chat.packet.client.ClientPacket;
import cv.bloody.ua.dream.chat.packet.client.PacketOutChatMessage;
import io.netty.channel.ChannelHandlerContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class UsersManager {

    private static final UsersManager instance = new UsersManager();

    public static UsersManager getInstance() {
        return instance;
    }

    private final List<User> userMap = new LinkedList<>();

    public void forEach(Consumer<User> consumer) {
        userMap.forEach(consumer);
    }

    public void authorizeUser(ChannelHandlerContext context , String login , String displayName , UserGroup group) {
        User user = new UserProfile(context , login , displayName , group);
        userMap.add(user);
        user.sendPacket(new AuthConfirmPacket(user));
        sendAll(new PacketOutChatMessage(new ServerMessage(ChatColor.ANSI_RED + user.getLogin()+ ChatColor.ANSI_GREEN + " is connected")));
        System.out.println(user.getLogin() + " is authorizate");
    }

    public User getUser(String login) {
        for(User u : userMap) {
            if(u.getLogin().equals(login)) return u;
        }
        return null;
    }

    public void sendAll(ClientPacket packet) {
        userMap.forEach(user -> {
            user.sendPacket(packet);
        });
    }

    public void inactivate(User user) {
        user.leave();
        userMap.remove(user);
    }
}
