package cv.bloody.ua.dream.chat.packet.server;

import cv.bloody.ua.dream.chat.users.User;

public abstract class ServerPacket {

    private final int id;

    public ServerPacket(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public abstract void handle() throws Exception;

    public void handle(User user) throws Exception {
        handle();
    }
}
