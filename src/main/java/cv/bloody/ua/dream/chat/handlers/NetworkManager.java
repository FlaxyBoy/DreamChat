package cv.bloody.ua.dream.chat.handlers;


import cv.bloody.ua.dream.chat.packet.server.AuthPacket;
import cv.bloody.ua.dream.chat.packet.server.PacketInChanMessage;
import cv.bloody.ua.dream.chat.packet.server.RegistrationPacket;
import cv.bloody.ua.dream.chat.packet.server.ServerPacket;
import io.netty.channel.ChannelHandlerContext;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class NetworkManager {

    private static final Map<Integer , BiFunction<JSONObject, ChannelHandlerContext , ServerPacket>>
            jsonToServerPacketReleaser = new HashMap<>();

    public static ServerPacket decode(String text , ChannelHandlerContext context) {
        JSONObject info = new JSONObject(text);
        if(!info.has("packet_id")) throw new IllegalStateException("packet not contains \"packet_id\"");
        int packetID = info.getInt("packet_id");
        BiFunction<JSONObject, ChannelHandlerContext , ServerPacket> function = jsonToServerPacketReleaser
                .getOrDefault(packetID , null);
        if(function == null) throw new IllegalStateException("illegal packet id");
        ServerPacket packet = function.apply(info , context);
        if(packet == null) throw new IllegalStateException("invalid packet state");
        return packet;
    }

    static {
        jsonToServerPacketReleaser.put(1 , (jsonObject, context) -> {
            try {
                return new AuthPacket(context , jsonObject.getString("login") , jsonObject.getString("password") , 1);
            }catch (Exception e) {
                return null;
            }
        });
        jsonToServerPacketReleaser.put(2 , (jsonObject, context) -> {
            try {
                return new RegistrationPacket(context , jsonObject.getString("login") , jsonObject.getString("password") , 2);
            }catch (Exception e) {
                return null;
            }
        });
        jsonToServerPacketReleaser.put(3 , (jsonObject, context) -> {
            try {
                return new PacketInChanMessage(context , jsonObject.getString("message") , 3);
            }catch (Exception e) {
                return null;
            }
        });
    }
}
