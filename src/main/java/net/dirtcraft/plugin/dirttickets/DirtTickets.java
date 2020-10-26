package net.dirtcraft.plugin.dirttickets;

import net.dirtcraft.plugin.dirttickets.Commands.CommandRegistry;
import net.dirtcraft.plugin.dirttickets.Database.Storage;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "dirt-tickets",
        name = "Dirt Tickets",
        description = "Integration with DirtCraft's ticket bot",
        url = "https://dirtcraft.net/",
        authors = "juliann"
)
public class DirtTickets {

    private Storage storage;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        this.storage = new Storage();

        new CommandRegistry(storage, this);
    }
}
