package cv.bloody.ua.dream.chat;

import java.util.logging.Logger;

public class DreamLogger {

    public void info(String message) {
        System.out.println("\u001B[30m[info\u001B[33m\u001B[30m]\u001B[0m " + message);
    }
}
