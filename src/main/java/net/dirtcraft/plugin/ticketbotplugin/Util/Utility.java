package net.dirtcraft.plugin.ticketbotplugin.Util;

import net.dirtcraft.discord.spongediscordlib.SpongeDiscordLib;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Utility {

    public static Text format(String message) {
        return TextSerializers.FORMATTING_CODE.deserialize(message);
    }

    public static String getServerCode() {
         String code = SpongeDiscordLib
                 .getJDA()
                 .getTextChannelById(
                         SpongeDiscordLib.getGamechatChannelID())
                 .getName().split("-")[1].toLowerCase();

         if (code.contains("sf4")) code = "sf4";

         return code;
    }

}