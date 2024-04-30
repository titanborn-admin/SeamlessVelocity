package com.velocitypowered.proxy.protocol.packet;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import com.velocitypowered.proxy.protocol.packet.entity.EntityPacket;
import io.netty.buffer.ByteBuf;
import java.util.UUID;

public class SpawnEntity implements MinecraftPacket, EntityPacket {

    private int entityId;
    private UUID uuid;
    private int type;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private float headYaw;
    private int data;
    private int velocityX;
    private int velocityY;
    private int velocityZ;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getHeadYaw() {
        return headYaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public int getData() {
        return data;
    }

    public int getEntityId() {
        return entityId;
    }

    public int getType() {
        return type;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public int getVelocityZ() {
        return velocityZ;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void setHeadYaw(float headYaw) {
        this.headYaw = headYaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public void setVelocityZ(int velocityZ) {
        this.velocityZ = velocityZ;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
        // TODO: versioning
        entityId = ProtocolUtils.readVarInt(buf);
        uuid = ProtocolUtils.readUuid(buf);
        type = ProtocolUtils.readVarInt(buf);
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        pitch = buf.readByte() * 360f / 256f;
        yaw = buf.readByte() * 360f / 256f;
        headYaw = buf.readByte() * 360f / 256f;
        data = ProtocolUtils.readVarInt(buf);
        velocityX = buf.readShort();
        velocityY = buf.readShort();
        velocityZ = buf.readShort();
    }

    @Override
    public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
        // TODO: versioning
        ProtocolUtils.writeVarInt(buf, entityId);
        ProtocolUtils.writeUuid(buf, uuid);
        ProtocolUtils.writeVarInt(buf, type);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeByte((int) (pitch * 256f / 360f));
        buf.writeByte((int) (yaw * 256f / 360f));
        buf.writeByte((int) (headYaw * 256f / 360f));
        ProtocolUtils.writeVarInt(buf, data);
        buf.writeShort(velocityX);
        buf.writeShort(velocityY);
        buf.writeShort(velocityZ);
    }

    @Override
    public boolean handle(MinecraftSessionHandler handler) {
        return handler.handle(this);
    }

}
