package support.kajstech.kajbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.reflections.Reflections;
import support.kajstech.kajbot.command.CommandManager;
import support.kajstech.kajbot.command.ICommand;
import support.kajstech.kajbot.handlers.ConfigHandler;
import support.kajstech.kajbot.handlers.CustomCommandsHandler;
import support.kajstech.kajbot.handlers.KeywordHandler;

import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class Bot {

    public static JDA jda;

    static void run() {

        KeywordHandler.init();
        CustomCommandsHandler.init();

        //JDA Builder
        JDABuilder builder = new JDABuilder(AccountType.BOT);


        builder.setToken(ConfigHandler.getProperty("Bot token"));
        builder.setGame(Game.playing(ConfigHandler.getProperty("Bot game")));

        //Loading commands
        Reflections cmdReflections = new Reflections("support.kajstech.kajbot.command.commands");
        Set<Class<? extends ICommand>> allCommands = cmdReflections.getSubTypesOf(ICommand.class);
        for (Class<? extends ICommand> command : allCommands) {
            try {
                CommandManager.addCommand(command.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        //Adding event listeners using ListenerAdaper
        Reflections listenerReflections = new Reflections("support.kajstech.kajbot.listeners");
        Set<Class<? extends ListenerAdapter>> allListeners = listenerReflections.getSubTypesOf(ListenerAdapter.class);
        for (Class<? extends ListenerAdapter> listener : allListeners) {
            try {
                builder.addEventListener(listener.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        //Random builder settings
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setAudioEnabled(false);

        //Building JDA
        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
