package com.jamo.hello;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.events.AddWorldEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jamo.hello.commands.FreezeCommand;
import com.jamo.hello.commands.LobbyCommand;
import com.jamo.hello.commands.SkywarsCommand;
import com.jamo.hello.components.FreezeComponent;
import com.jamo.hello.systems.FreezeSystem;

import javax.annotation.Nonnull;

public final class HelloPlugin extends JavaPlugin {
    public static HelloPlugin instance;
    public static ComponentType<EntityStore, FreezeComponent> freezeComponent;

    public HelloPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }


    @Override
    protected void setup() {
        getLogger().at(java.util.logging.Level.INFO).log("HelloPlugin setup()");

        // Component registration
        freezeComponent = getEntityStoreRegistry().registerComponent(FreezeComponent.class, FreezeComponent::new);

        // Command registration
        getCommandRegistry().registerCommand(new HelloCommand());
        getCommandRegistry().registerCommand(new SkywarsCommand());
        getCommandRegistry().registerCommand(new LobbyCommand());
        getCommandRegistry().registerCommand(new FreezeCommand());

        // System registration
        getEntityStoreRegistry().registerSystem(new HelloBreakBlockSystem());
        getEntityStoreRegistry().registerSystem(new FreezeSystem());
    }

    @Override
    protected void start() {
        getLogger().at(java.util.logging.Level.INFO).log("HelloPlugin start()");
    }

    public static HelloPlugin get() {
        return instance;
    }

    @Override
    protected void shutdown() {
        getLogger().at(java.util.logging.Level.INFO).log("HelloPlugin shutdown()");
    }
}

