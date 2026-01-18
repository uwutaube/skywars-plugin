package com.jamo.hello;

import com.hypixel.hytale.builtin.instances.InstancesPlugin;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;

import java.util.ArrayList;
import java.util.List;

enum LobbyState {WAITING, COUNTDOWN, STARTED, FINISHED};

public class LobbyManager {
    private final List<SkywarsLobby> lobbies = new ArrayList<>();

    public synchronized SkywarsLobby findOrCreateLobby() {
        for (SkywarsLobby lobby : lobbies) {
            if (lobby.getState() == LobbyState.WAITING && lobby.size() < SkywarsLobby.MAX_PLAYERS) {
                return lobby;
            }
        }

        World newLobbyWorld = createWorld();
        newLobbyWorld.getWorldConfig().setDeleteOnRemove(true);
        newLobbyWorld.getWorldConfig().markChanged(); // save config

        SkywarsLobby newLobby = new SkywarsLobby(newLobbyWorld);
        lobbies.add(newLobby);

        return newLobby;
    }

    private World createWorld() {
        World defaultWorld = Universe.get().getDefaultWorld();
        assert defaultWorld != null;

        World newWorld = InstancesPlugin.get().spawnInstance("Forgotten_Temple", defaultWorld, new Transform(0, 0, 0)).join();

        return newWorld;
    }

    public synchronized void removeLobby(SkywarsLobby lobby) {
        lobbies.remove(lobby);
    }
}