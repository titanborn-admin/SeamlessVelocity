package com.velocitypowered.proxy.event.connection;

import com.velocitypowered.api.event.annotation.AwaitingEvent;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import org.jetbrains.annotations.NotNull;

@AwaitingEvent
public final class ConnectionPacketEvent {

    private final @NotNull MinecraftSessionHandler sessionHandler;
    private @NotNull MinecraftPacket packet;

    public ConnectionPacketEvent(@NotNull MinecraftSessionHandler sessionHandler, @NotNull MinecraftPacket packet) {
        this.sessionHandler = sessionHandler;
        this.packet = packet;
    }

    public @NotNull MinecraftSessionHandler getSessionHandler() {
        return sessionHandler;
    }

    public @NotNull MinecraftPacket getPacket() {
        return packet;
    }

    public void setPacket(@NotNull MinecraftPacket packet) {
        this.packet = packet;
    }

}
