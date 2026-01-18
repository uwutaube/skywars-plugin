package com.jamo.hello.components;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jamo.hello.HelloPlugin;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;


public class FreezeComponent implements Component<EntityStore> {
    public static ComponentType<EntityStore, FreezeComponent> getComponentType() {
        return HelloPlugin.freezeComponent;
    }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new FreezeComponent();
    }

    public static void resetMovementSettings(Store<EntityStore> store, Ref<EntityStore> ref) {
        MovementManager movementManager = store.getComponent(ref, MovementManager.getComponentType());
        assert movementManager != null;

        movementManager.resetDefaultsAndUpdate(ref, store);
    }
}
