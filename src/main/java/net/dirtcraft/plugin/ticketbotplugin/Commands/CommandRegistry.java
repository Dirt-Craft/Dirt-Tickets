package net.dirtcraft.plugin.ticketbotplugin.Commands;

import net.dirtcraft.plugin.ticketbotplugin.Database.Storage;
import net.dirtcraft.plugin.ticketbotplugin.DirtTickets;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandRegistry {

    private final Storage storage;
    private final DirtTickets main;

    public CommandRegistry(Storage storage, DirtTickets main) {
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
                .executor(new List(main, storage))
                .build();
    }

    private CommandSpec create() {
        return CommandSpec.builder()
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("reason")))
                .executor(new Create(main, storage))
                .build();
    }

}
