package com.jamo.hello.utils;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class TeleportUtils {
    public static void teleportToWorld(PlayerRef playerRef, World targetWorld, Transform spawnPoint) {
        Ref<EntityStore> ref = playerRef.getReference();
        Store<EntityStore> store = ref.getStore();
        World currentWorld = store.getExternalData().getWorld();

        currentWorld.execute(() -> {
            store.addComponent(ref, Teleport.getComponentType(), new Teleport(targetWorld, spawnPoint));
        });
    }
}

/*
```
 Vector3d spawnPos = new Vector3d(0.5, 64, 0.5);
 Vector3f spawnRot = new Vector3f(0, 0, 0); // No rotation

 // Create teleport to world
 Teleport teleport = new Teleport(world, spawnPos, spawnRot);
```
*/