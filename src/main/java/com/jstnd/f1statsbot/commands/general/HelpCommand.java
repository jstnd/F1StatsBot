package com.jstnd.f1statsbot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class HelpCommand extends Command {

    public HelpCommand() {
        this.name = "help";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.reply(getCommandList());
            return;
        }

        if (event.getArgs().equals("help")) {
            return;
        }

        for (Command c: event.getClient().getCommands()) {
            if (event.getArgs().equals(c.getName())) {
                event.reply(c.getHelp());
                return;
            }
        }
    }

    private String getCommandList() {
        return "```General:\n" +
                "\thelp         Shows this message.\n" +
                "Main:\n" +
                "\tcircuit      Displays information about a specified circuit.\n" +
                "\tcircuits     Displays list of circuits and their id's for reference purposes.\n" +
                "\tconstructor  Displays information about a specified constructor.\n" +
                "\tconstructors Displays list of constructors and their id's for reference purposes.\n" +
                "\tdriver       Displays information about a specified driver.\n" +
                "\tdrivers      Displays list of drivers and their id's for reference purposes.\n" +
                "\tnext         Displays information about the upcoming F1 race.\n" +
                "\tqualifying   Displays qualifying results for an F1 race.\n" +
                "\trace         Displays driver results of an F1 race.\n" +
                "\tschedule     Displays race schedule of an F1 season.\n" +
                "\tseason       Displays constructor and driver standings of an F1 season.\n\n" +
                "Type .f1 help [command] for additional information about any of the main commands.\n" +
                "Alternatively, contact Justin#0285.```";
    }
}
