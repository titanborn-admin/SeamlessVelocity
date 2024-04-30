package com.velocitypowered.proxy.protocol.packet;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import com.velocitypowered.proxy.protocol.packet.entity.EntityPacket;
import io.netty.buffer.ByteBuf;

public class EntitySoundEffect implements MinecraftPacket, EntityPacket {

    private int soundId;
    private int soundCategory;
    private int entityId;
    private float volume;
    private float pitch;
    private long seed;

    @Override
    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }

    public int getSoundCategory() {
        return soundCategory;
    }

    public int getSoundId() {
        return soundId;
    }

    public long getSeed() {
        return seed;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setSoundCategory(int soundCategory) {
        this.soundCategory = soundCategory;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
        this.soundId = ProtocolUtils.readVarInt(buf);
        this.soundCategory = ProtocolUtils.readVarInt(buf);
        this.entityId = ProtocolUtils.readVarInt(buf);
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
        this.seed = buf.readLong();
    }

    @Override
    public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
        ProtocolUtils.writeVarInt(buf, soundId);
        ProtocolUtils.writeVarInt(buf, soundCategory);
        ProtocolUtils.writeVarInt(buf, entityId);
        buf.writeFloat(volume);
        buf.writeFloat(pitch);
        buf.writeLong(seed);
    }

    @Override
    public boolean handle(MinecraftSessionHandler handler) {
        return handler.handle(this);
    }

}
