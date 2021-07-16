package tech.jimmystone.chatter.discord;

import com.velocitypowered.api.proxy.ProxyServer;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;

import static tech.jimmystone.chatter.Chatter.*;

public class DiscordHandler extends ListenerAdapter {

    private static ProxyServer proxy;

    public DiscordHandler() {
    }

    public DiscordHandler(ProxyServer ps) {
        proxy = ps;
    }

    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getGuild() == null) return;
        if (event.getName().equals("ping")) {
            event.reply("pong").queue();
        }
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getChannel().getId().equals("865037740420759573")) {
            proxy.getAllPlayers().forEach(target -> target.sendMessage(Component.text(event.getMessage().getContentRaw())));
        }
    }
}
