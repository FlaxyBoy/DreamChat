package cv.bloody.ua.dream.chat.packet.client;

import org.json.JSONObject;

public abstract class ClientPacket {

    protected static enum PacketIDEnum {
        AUTH_FAILED_PACKET,
        REGISTER_FAILED_PACKET,
        AUTH_CONFIRM_PACKET,
        MESSAGE_PACKET,
        PRIVATE_MESSAGE_PACKET;
    }

    public abstract JSONObject toJSON();
}
