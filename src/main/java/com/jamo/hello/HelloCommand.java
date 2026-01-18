package com.jamo.hello;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

public final class HelloCommand extends CommandBase {
    public HelloCommand() {
        super("hello", "Says hello");
    }

    @Override
    protected void executeSync(CommandContext ctx) {
        ctx.sendMessage(Message.raw("Hello!"));
    }
}

