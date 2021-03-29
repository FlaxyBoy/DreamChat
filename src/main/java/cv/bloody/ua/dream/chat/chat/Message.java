package cv.bloody.ua.dream.chat.chat;

import cv.bloody.ua.dream.chat.users.UserGroup;

public interface Message {

    String getSender();

    String getDisplayName();

    UserGroup getUserGroup();

    String getMessage();

    long getDate();
}
