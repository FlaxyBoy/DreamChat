package cv.bloody.ua.dream.chat.users;

import cv.bloody.ua.dream.chat.chat.Message;

public class ServerMessage implements Message {

    private String message;

    public ServerMessage(String message) {
        this.message = message;
    }

    @Override
    public String getSender() {
        return "$server";
    }

    @Override
    public String getDisplayName() {
        return "@null";
    }

    @Override
    public UserGroup getUserGroup() {
        return UserGroup.ADMINISTRATOR;
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
