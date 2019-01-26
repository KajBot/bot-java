package support.kajstech.kajbot.command.commands;


import support.kajstech.kajbot.command.Command;
import support.kajstech.kajbot.command.CommandEvent;
import support.kajstech.kajbot.handlers.ConfigHandler;
import support.kajstech.kajbot.utils.LogHelper;

public class Invite extends Command {
    public Invite() {
        this.name = "invite";
        this.guildOnly = true;
        this.requiredRole = ConfigHandler.getProperty("Bot admin role");
    }

    @Override
    protected void execute(CommandEvent e) {
        try {
            e.reply(e.getGuild().getTextChannelById(e.getChannel().getIdLong()).createInvite().setUnique(true).setMaxUses(1).setMaxAge(1800).complete().getUrl());
        } catch (Exception ex) {
            LogHelper.error(this.getClass(), ex, e.getMessage().getContentRaw());
        }
    }
}
