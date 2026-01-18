package com.jamo.hello;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.MovementSettings;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jamo.hello.components.FreezeComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SkywarsLobby {
    private final List<PlayerRef> players = new ArrayList<>();
    private LobbyState state = LobbyState.WAITING;
    public final World world;
    private volatile ScheduledFuture<?> countdownTask = null;

    public SkywarsLobby(World world) {
        this.world = world;
    }

    private static final int MIN_PLAYERS = 1;
    public static final int MAX_PLAYERS = 8;
    private static final int COUNTDOWN_SECONDS = 15;

    public synchronized int size() {
        return players.size();
    }

    public synchronized LobbyState getState() {
        return state;
    }

    public synchronized boolean addPlayer(PlayerRef player) {
        if (state != LobbyState.WAITING) return false;
        if (players.contains(player)) return false;
        if (players.size() >= MAX_PLAYERS) return false;

        players.add(player);
        checkStartConditions();

        world.sendMessage(Message.raw("SKYWARS >>> PLAYER JOINED " + player.getUsername()));
        return true;
    }

    private void checkStartConditions() {
        if (state == LobbyState.WAITING && players.size() >= MIN_PLAYERS) {
            startCountdown();
        }
    }

    private void startCountdown() {
        System.out.println("Starting countdown...");

        synchronized (this) {
            if (state != LobbyState.WAITING) return;
            if (countdownTask != null) return;
            state = LobbyState.COUNTDOWN;
        }

        // freezeAllPlayers();

        AtomicInteger remaining = new AtomicInteger(COUNTDOWN_SECONDS);

        Runnable tick = () -> {
            int left = remaining.getAndDecrement();

            synchronized (SkywarsLobby.this) {
                if (players.size() < MIN_PLAYERS) {
                    cancelCountdown();
                    return;
                }
            }

            if (left <= 0) {
                synchronized (SkywarsLobby.this) {
                    if (players.size() >= MIN_PLAYERS) {
                        startGame();
                        cancelCountdown();
                        state = LobbyState.STARTED;
                    } else {
                        cancelCountdown();
                        System.out.println("Not enough players, canceling countdown");
                    }
                }
                return;
            }
            if (left == 10 || left <= 5) {
                System.out.println("Countdown: " + left);
            }
        };

        countdownTask = HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(tick, 0, 1, TimeUnit.SECONDS);
    }

    private void freezeAllPlayers() {
        System.out.println("Frozening all players...");

        for (PlayerRef player : players) {
            Ref<EntityStore> ref = player.getReference();
            if (ref == null || !ref.isValid()) continue;
            Store<EntityStore> store = ref.getStore();

            MovementManager movementManager = store.getComponent(ref, MovementManager.getComponentType());
            if (movementManager == null) continue;

            System.out.println("Frozen " + player.getUsername());
            MovementSettings settings = movementManager.getSettings();
            System.out.println("Base speed " + settings.baseSpeed);
            settings.baseSpeed = 0;

            movementManager.update(player.getPacketHandler());
        }
    }

    private synchronized void cancelCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel(false);
            countdownTask = null;
        }
        state = LobbyState.WAITING;
    }

    private void startGame() {
        world.sendMessage(Message.raw("SKYWARS >>> COUNTDOWN OVER STARTING GAME!"));

        /*

        for (PlayerRef player : players) {
            Ref<EntityStore> ref = player.getReference();
            if (ref == null) continue;

            Store<EntityStore> store = ref.getStore();

            // store.removeComponent(ref, FreezeComponent.getComponentType());
            // FreezeComponent.resetMovementSettings(store, ref);
        }
         */
    }
}