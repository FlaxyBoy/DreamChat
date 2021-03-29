package cv.bloody.ua.dream.chat.users;

import cv.bloody.ua.dream.chat.chat.ChatColor;

public enum UserGroup {
    USER("Користувач" , ChatColor.ANSI_YELLOW + " U"),
    MODERATOR("Модератор" , ChatColor.ANSI_BLUE + " MOD"),
    ADMINISTRATOR("Адміністратор" , ChatColor.ANSI_BLUE + " ADM");


    private UserGroup(String displayName , String prefix) {

    }
}
