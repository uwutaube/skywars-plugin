package com.jamo.hello.commands;

import com.hypixel.hytale.builtin.instances.InstancesPlugin;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.protocol.MovementSettings;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jamo.hello.LobbyManager;
import com.jamo.hello.SkywarsLobby;
import com.jamo.hello.components.FreezeComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.UUID;

public class SkywarsCommand extends AbstractPlayerCommand {
    public SkywarsCommand() {
        super("skywars", "play skywars");
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        Player player = store.getComponent(ref, Player.getComponentType());
        UUID uuid = playerRef.getUuid();

        assert player != null;
        player.sendMessage(Message.raw("You executed skywars"));

        LobbyManager manager = new LobbyManager();
        SkywarsLobby skywarsLobby = manager.findOrCreateLobby();

        // store.addComponent(ref, FreezeComponent.getComponentType(), new FreezeComponent());

        InstancesPlugin.teleportPlayerToInstance(ref, store, skywarsLobby.world, null);

        MovementManager movementManager = store.getComponent(ref, MovementManager.getComponentType());
        MovementSettings settings = movementManager.getSettings();
        settings.baseSpeed = 0;
        movementManager.update(playerRef.getPacketHandler());

    }
}
