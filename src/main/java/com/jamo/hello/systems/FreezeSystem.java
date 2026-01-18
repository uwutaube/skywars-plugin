package com.jamo.hello.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.HolderSystem;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.protocol.MovementSettings;
import com.hypixel.hytale.protocol.packets.player.UpdateMovementSettings;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerMovementManagerSystems;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jamo.hello.components.FreezeComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import java.util.Set;

public class FreezeSystem extends HolderSystem<EntityStore> {
    @Override
    public void onEntityAdd(@NonNullDecl Holder<EntityStore> holder, @NonNullDecl AddReason addReason, @NonNullDecl Store<EntityStore> store) {
        holder.ensureComponent(MovementManager.getComponentType());
        holder.ensureComponent(PlayerRef.getComponentType());

        // Apply freeze - use holder directly
        MovementManager movementManager = holder.getComponent(MovementManager.getComponentType());
        if (movementManager != null) {
            MovementSettings freezeSettings = new MovementSettings(movementManager.getSettings());
            freezeSettings.baseSpeed = 0;
            freezeSettings.acceleration = 0;

            PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
            if (playerRef != null) {
                playerRef.getPacketHandler().writeNoCache(new UpdateMovementSettings(freezeSettings));
            }

            System.out.println("BASE SPEED: " + freezeSettings.acceleration);
            System.out.println("ACC " + freezeSettings.baseSpeed);
        }
    }

    @Override
    public void onEntityRemoved(@NonNullDecl Holder<EntityStore> holder, @NonNullDecl RemoveReason removeReason, @NonNullDecl Store<EntityStore> store) {
        // Restore defaults
        MovementManager movementManager = holder.getComponent(MovementManager.getComponentType());

        if (movementManager != null) {
            movementManager.applyDefaultSettings();
            PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
            if (playerRef != null) {
                movementManager.update(playerRef.getPacketHandler());
            }
        }
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(PlayerRef.getComponentType(), FreezeComponent.getComponentType());
    }

    private final Set<Dependency<EntityStore>> dependencies = Set.of(
            new SystemDependency(Order.AFTER, PlayerSystems.PlayerAddedSystem.class),
            new SystemDependency(Order.AFTER, PlayerMovementManagerSystems.AssignmentSystem.class)
    );

    @Nonnull
    @Override
    public Set<Dependency<EntityStore>> getDependencies() {
        return dependencies;
    }
}
