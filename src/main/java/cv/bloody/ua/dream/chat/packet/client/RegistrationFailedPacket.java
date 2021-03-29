package cv.bloody.ua.dream.chat.packet.client;

import org.json.JSONObject;

public class RegistrationFailedPacket extends ClientPacket {

    private final Reason reason;

    public RegistrationFailedPacket(Reason reason) {
        this.reason = reason;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject().put("packet_id" , PacketIDEnum.REGISTER_FAILED_PACKET.ordinal()).put("reason" , reason.name());
    }


    public static enum Reason {
        WRONG_FORMAT_LOGIN,
        TO_MANY_ADDRES,
        LOGIN_IS_OCCUPIED,
        SERVER_PROBLEM;
    }

}
