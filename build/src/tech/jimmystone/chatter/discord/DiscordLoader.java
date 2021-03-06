package tech.jimmystone.chatter.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import javax.security.auth.login.LoginException;

public class DiscordLoader {

    private static JDA jda;

    public static void startBot(String token) throws LoginException {
        // slash commands don't need intents
        jda = JDABuilder.createDefault(token)
                .addEventListeners(new DiscordHandler())
                .build();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                new CommandData("ping", "ping to test this bot ig")
        ).queue();
    }

    public static JDA getJDA() {
        return jda;
    }
}
