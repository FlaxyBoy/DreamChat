package cv.bloody.ua.dream.chat.users;

import cv.bloody.ua.dream.chat.packet.client.ClientPacket;

public interface User {

    String getLogin();

    UserGroup getGroup();

    String getDisplayName();

    boolean isOnline();

    void sendPacket(ClientPacket packet);

    void leave();

}
