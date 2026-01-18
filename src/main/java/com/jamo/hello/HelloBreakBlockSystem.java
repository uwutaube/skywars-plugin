package com.jamo.hello;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.io.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class HelloBreakBlockSystem extends EntityEventSystem<EntityStore, BreakBlockEvent> {
    private static final Query<EntityStore> QUERY = Archetype.of(Player.getComponentType());

    public HelloBreakBlockSystem() {
        super(BreakBlockEvent.class);
    }

    @Override
    public Query<EntityStore> getQuery() {
        return QUERY;
    }

    @Override
    public void handle(
            int entityIndex,
            ArchetypeChunk<EntityStore> chunk,
            Store<EntityStore> store,
            CommandBuffer<EntityStore> commandBuffer,
            BreakBlockEvent event
    ) {
        Player player = chunk.getComponent(entityIndex, Player.getComponentType());
        if (player == null) {
            return;
        }

        EntityStore entityStore = store.getExternalData();
        World world = entityStore != null ? entityStore.getWorld() : null;

        Vector3i pos = event.getTargetBlock();
        BlockType blockType = event.getBlockType();

        String playerName = player.getDisplayName();
        String worldName = (world != null) ? world.getName() : "unknown-world";
        String blockId = (blockType != null && blockType.getId() != null && !blockType.getId().isEmpty())
                ? blockType.getId()
                : "unknown-block";

        int x = (pos != null) ? pos.getX() : 0;
        int y = (pos != null) ? pos.getY() : 0;
        int z = (pos != null) ? pos.getZ() : 0;

        String text = "Hello! " + playerName + " just broke a " + blockId + " block in " + worldName + " at " + x + ", " + y + ", " + z;
        player.sendMessage(Message.raw(text));
    }
}

