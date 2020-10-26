package net.dirtcraft.plugin.dirttickets.Commands;

import net.dirtcraft.plugin.dirttickets.Database.Storage;
import net.dirtcraft.plugin.dirttickets.DirtTickets;
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
                .child(this.info(), "info")
                .build();
    }

    private CommandSpec list() {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.optional(
                                GenericArguments.requiringPermission(
                                        GenericArguments.user(Text.of("user")),
                                        "dirttickets.other")),
                        GenericArguments.flags().flag("a").buildWith(GenericArguments.none()))
                .executor(new List(main, storage))
                .build();
    }

    private CommandSpec create() {
        return CommandSpec.builder()
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("reason")))
                .executor(new Create(main, storage))
                .build();
    }

    private CommandSpec info() {
        return CommandSpec.builder()
                .arguments(GenericArguments.integer(Text.of("id")))
                .executor(new Info(main, storage))
                .build();
    }

}
