package cv.bloody.ua.dream.chat.chat;

public enum ChatColor {
    ANSI_RESET("\u001B[0m" , "r"),
    ANSI_BLACK("\u001B[31m" , "1"),
    ANSI_RED("\u001B[31m" ,"2"),
    ANSI_GREEN("\u001B[32m" , "3"),
    ANSI_YELLOW("\u001B[33m" , "4"),
    ANSI_BLUE("\u001B[34m" , "5"),
    ANSI_PURPLE("\u001B[35m" , "6"),
    ANSI_CYAN("\u001B[36m" , "7"),
    ANSI_WHITE("\u001B[37m" , "8");

    private final String text;
    private final String replace;

    private ChatColor(String text , String replace) {
        this.text = text;
        this.replace = replace;
    }

    public static String setColor(String color) {
        for(ChatColor c : values()) {
            color = color.replace("§" + c.replace , c.text);
            color = color.replace("&" + c.replace , c.text);
        }
        return color;
    }


    @Override
    public String toString() {
        return text;
    }
}
