package cv.bloody.ua.dream.chat.packet.client;

import org.json.JSONObject;

public class AuthorizeFailedPacket extends ClientPacket {

    private final Reason reason;

    public AuthorizeFailedPacket(Reason reason) {
        this.reason = reason;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject().put("packet_id" , PacketIDEnum.AUTH_FAILED_PACKET.ordinal()).put("reason" , reason.name());
    }


    public static enum Reason {
        UNKNOWN_USER,
        FAILED_PASSWORD,
        USER_IS_LOGINED,
        SERVER_PROBLEM;
    }

}
