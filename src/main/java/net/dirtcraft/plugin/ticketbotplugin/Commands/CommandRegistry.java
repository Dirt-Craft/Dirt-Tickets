package net.dirtcraft.plugin.ticketbotplugin.Commands;

import net.dirtcraft.plugin.ticketbotplugin.Database.Storage;
import net.dirtcraft.plugin.ticketbotplugin.TicketBotPlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;

public class CommandRegistry {

    private final Storage storage;
    private final TicketBotPlugin main;

    public CommandRegistry(Storage storage, TicketBotPlugin main) {
        this.storage = storage;
        this.main = main;

        Sponge.getCommandManager().register(main, this.base(), "ticket");
        Sponge.getCommandManager().register(main, this.list(), "tickets");
    }

    private CommandSpec base() {
        return CommandSpec.builder()
                .child(this.list(), "list")
                .child(this.create(), "create")
                .build();
    }

    private CommandSpec list() {
        return CommandSpec.builder()
                .executor(new List(storage))
                .build();
    }

    private CommandSpec create() {
        return CommandSpec.builder()
                .executor(new Create(storage))
                .build();
    }

}
