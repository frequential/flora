package me.jasperedits.flora.command.impl.legacy;

import me.jasperedits.flora.command.Command;
import me.jasperedits.flora.command.ExecutionData;
import me.jasperedits.flora.command.annotation.CommandNames;
import me.jasperedits.flora.command.annotation.CommandType;
import me.jasperedits.flora.command.annotation.CommandType.Allowance;
import me.jasperedits.flora.command.annotation.CommandType.Format;
import me.jasperedits.flora.command.annotation.LonelyCommand;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

@CommandType(
        format = Format.LEGACY,
        allowance = Allowance.BOT_OWNER
)
@CommandNames("update")
public class UpdateGuildCommands extends Command {

    @LonelyCommand
    public void UpdateGuildCommands(ExecutionData information) {
        information.getLegacyEvent().getGuild().updateCommands().addCommands(
                        new CommandData("prefix", "Changes the legacy prefix on this guild.")
                                .addOption(OptionType.STRING, "prefix", "a new prefix", true),
                        new CommandData("privacy", "A small privacy notice."),
                        new CommandData("config", "Sets Flora up.")
                                .addSubcommands(new SubcommandData("objectivechannel", "Change this guild's message objective channel.")
                                        .addOption(OptionType.CHANNEL, "channel", "The text channel where Flora will send the message objective.", true))
                                .addSubcommands(new SubcommandData("language", "Change the language of this guild or get a list of available languages.")
                                        .addOption(OptionType.STRING, "code", "A valid language code.")),
                        new CommandData("setup", "Get quick tutorials on how to set Flora up."),
                        new CommandData("ping", "Ping Flora."),
                        new CommandData("start", "Start community goals.")
                                .addSubcommandGroups(new SubcommandGroupData("messages", "Start message community goals.")
                                                .addSubcommands(new SubcommandData("goal", "A simple and automatic message goal.")
                                                        .addOption(OptionType.STRING, "prize", "Add a prize to incentivise your users.")),
                                        new SubcommandGroupData("user", "Start user community goals.")
                                                .addSubcommands(new SubcommandData("goal", "A simple and automatic user goal."))))
                .queue();

        information.getLegacyEvent().getMessage().reply("(<:flora_devMode:876605895264571434>) Flora requested to update guild commands to Discord.").mentionRepliedUser(false).queue();
    }
}
