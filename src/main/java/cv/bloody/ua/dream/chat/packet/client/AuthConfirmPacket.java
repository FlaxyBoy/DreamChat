package cv.bloody.ua.dream.chat.packet.client;

import cv.bloody.ua.dream.chat.users.User;
import org.json.JSONObject;

public class AuthConfirmPacket extends ClientPacket{

    private final User user;

    public AuthConfirmPacket(User user) {
        this.user = user;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject().put("packet_id" ,
                PacketIDEnum.AUTH_CONFIRM_PACKET.ordinal())
                .put("display_name" , user.getDisplayName())
                .put("group" , user.getGroup());
    }
}
