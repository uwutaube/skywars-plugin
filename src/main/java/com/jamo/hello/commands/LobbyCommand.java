package com.jamo.hello.commands;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.jamo.hello.utils.TeleportUtils;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.UUID;

public class LobbyCommand extends CommandBase {
    public LobbyCommand() {
        super("hub", "Back to hub");
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        UUID playerUUID = commandContext.sender().getUuid();
        PlayerRef playerRef = Universe.get().getPlayer(playerUUID);

        if (playerRef == null) return;

        World lobby = Universe.get().getWorld("default");

        TeleportUtils.teleportToWorld(playerRef, lobby, new Transform(0, 0, 0));
    }
}
