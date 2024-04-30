package com.velocitypowered.proxy.protocol.packet;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import com.velocitypowered.proxy.protocol.packet.entity.EntityPacket;
import io.netty.buffer.ByteBuf;

public class EntityAnimation implements MinecraftPacket, EntityPacket {

    private int entityId;
    private byte animation;

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public byte getAnimation() {
        return animation;
    }

    public void setAnimation(byte animation) {
        this.animation = animation;
    }

    @Override
    public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
        this.entityId = ProtocolUtils.readVarInt(buf);
        this.animation = buf.readByte(); // TODO: is this an unsigned byte?
    }

    @Override
    public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
        ProtocolUtils.writeVarInt(buf, entityId);
        buf.writeByte(animation);
    }

    @Override
    public boolean handle(MinecraftSessionHandler handler) {
        return handler.handle(this);
    }

}
