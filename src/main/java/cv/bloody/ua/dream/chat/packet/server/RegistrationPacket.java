package cv.bloody.ua.dream.chat.packet.server;


import cv.bloody.ua.dream.chat.ServerApp;
import cv.bloody.ua.dream.chat.users.User;
import cv.bloody.ua.dream.chat.users.UserGroup;
import cv.bloody.ua.dream.chat.users.UsersManager;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

public class RegistrationPacket extends ServerPacket {

    private static final Pattern pattern = Pattern.compile("[a-zA-Z1-9]");

    private final ChannelHandlerContext context;
    private final String login;
    private final String password;

    public RegistrationPacket(ChannelHandlerContext context , String login , String password , int id) {
        super(id);
        this.login = login.toLowerCase();
        this.context = context;
        this.password = password;
    }

    private User user;

    @Override
    public void handle() throws Exception {
        if(login.length() > 20) throw new IllegalArgumentException("invalid login");
        if(pattern.matcher(login).matches()) throw new IllegalArgumentException("invalid login");
        PreparedStatement statement = ServerApp.getPool().getSelectByName();
        statement.setString(1 , login);
        ResultSet set = statement.executeQuery();
        if(set.next()) {
            set.close();
            throw new IllegalStateException();
        }
        set.close();
        statement = ServerApp.getPool().getSelectByAddres();
        String addres =  ((InetSocketAddress) context.channel().remoteAddress()).getAddress().getHostAddress();
        statement.setString(1 , addres);
        set = statement.executeQuery();
        int addreses = 0;
        while (set.next()) addreses++;
        set.close();
        if(addreses >= 3) throw new IllegalArgumentException("to many addres");
        statement = ServerApp.getPool().getInsertUser();
        statement.setString(1 , login);
        statement.setString(2 , decodeToSha256(password));
        statement.setLong(3 , System.currentTimeMillis());
        statement.setString(4 , addres);
        statement.setString(5 , "@not_have");
        statement.executeUpdate();
        UsersManager.getInstance().authorizeUser(context , login , "@not_have" , UserGroup.USER);
        user = UsersManager.getInstance().getUser(login);
    }

    public User getUser() {
        return user;
    }

    public static String decodeToSha256(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
        for (int i = 0; i < encodedhash.length; i++) {
            String hex = Integer.toHexString(0xff & encodedhash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
