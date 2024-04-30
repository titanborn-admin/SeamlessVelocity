package com.velocitypowered.proxy.protocol.packet;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;

public class RemoveEntities implements MinecraftPacket {

    private List<Integer> entities;

    public List<Integer> getEntities() {
        return entities;
    }

    public void setEntities(List<Integer> entities) {
        this.entities = entities;
    }

    @Override
    public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
        int size = ProtocolUtils.readVarInt(buf);
        List<Integer> entities = new ArrayList<>(size);
        for (int i = 0; i < size; i++) entities.add(ProtocolUtils.readVarInt(buf));
        this.entities = entities;
    }

    @Override
    public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
        ProtocolUtils.writeVarInt(buf, entities.size());
        for (int entity : entities) ProtocolUtils.writeVarInt(buf, entity);
    }

    @Override
    public boolean handle(MinecraftSessionHandler handler) {
        return handler.handle(this);
    }

}
