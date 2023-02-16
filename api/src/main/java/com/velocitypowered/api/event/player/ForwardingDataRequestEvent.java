package com.velocitypowered.api.event.player;

import com.velocitypowered.api.event.annotation.AwaitingEvent;
import com.velocitypowered.api.util.GameProfile;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@AwaitingEvent
public class ForwardingDataRequestEvent {

    private final @NotNull List<GameProfile.Property> properties;

    public ForwardingDataRequestEvent(@NotNull List<GameProfile.Property> properties) {
        this.properties = new ArrayList<>(properties);
    }

    public @NotNull List<GameProfile.Property> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "ForwardingDataRequestEvent{" +
                "properties=" + properties +
                '}';
    }

}
