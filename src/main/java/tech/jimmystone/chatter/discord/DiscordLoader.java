package tech.jimmystone.chatter.discord;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.PingCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import javax.security.auth.login.LoginException;

public class DiscordLoader {

    private static JDA jda;

    public static void startBot(String token) throws LoginException {
        // slash commands don't need intents
        EventWaiter waiter = new EventWaiter();
        CommandClientBuilder client = new CommandClientBuilder();

        client.useDefaultGame();
        client.setOwnerId("519470107379105792");
        client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
        client.setPrefix("!");
        client.addCommands(new PingCommand());

        jda = JDABuilder.createDefault(token)
                .addEventListeners(waiter, client.build())
                .addEventListeners(new DiscordHandler())
                .build();

//        CommandListUpdateAction commands = jda.updateCommands();
//
//        commands.addCommands(
//                new CommandData("ping", "ping to test this bot ig")
//        ).queue();
    }

    public static JDA getJDA() {
        return jda;
    }
}
