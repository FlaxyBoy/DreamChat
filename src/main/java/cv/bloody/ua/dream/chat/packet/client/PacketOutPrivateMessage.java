package cv.bloody.ua.dream.chat.packet.client;

import cv.bloody.ua.dream.chat.chat.Message;
import org.json.JSONObject;

public class PacketOutPrivateMessage extends ClientPacket{

    private final Message message;

    public PacketOutPrivateMessage(Message message) {
        this.message = message;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject().put("packet_id" , ClientPacket.PacketIDEnum.PRIVATE_MESSAGE_PACKET.ordinal())
                .put("sender" , message.getSender()).put("display_name" , message.getDisplayName())
                .put("message" , message.getMessage()).put("date" , message.getDate())
                .put("group" , message.getUserGroup().name());
    }

}
