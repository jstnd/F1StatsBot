package com.jstnd.f1statsbot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jstnd.f1statsbot.commands.f1.*;
import com.jstnd.f1statsbot.commands.general.HelpCommand;
import com.jstnd.f1statsbot.database.Database;
import com.jstnd.f1statsbot.ergastapi.ErgastApi;
import com.jstnd.f1statsbot.util.Constants;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

// Project began February 2, 2020

public class F1StatsBot {
    public static void main(String[] args) throws LoginException {

        Config config = ConfigFactory.load();

        ErgastApi api = new ErgastApi();
        Database db = new Database(config.getString("database.url"),
                                   config.getString("database.username"),
                                   config.getString("database.password"));

        CommandClient client = new CommandClientBuilder()
                .setPrefix(config.getString("prefix"))
                .setAlternativePrefix(config.getString("alt-prefix"))
                .setOwnerId(config.getString("owner-id"))
                .setActivity(Activity.playing(Constants.FLAG + " '.f1 help' for help " + Constants.FLAG))
                .setEmojis(Constants.FLAG, Constants.WARNING, Constants.ERROR)
                .useHelpBuilder(false)
                .addCommands(new HelpCommand(),
                             new CircuitCommand(db),
                             new CircuitsCommand(db),
                             new ConstructorCommand(db),
                             new ConstructorsCommand(db),
                             new DriverCommand(db),
                             new DriversCommand(db),
                             new NextCommand(api, db),
                             new QualifyingCommand(api, db),
                             new RaceCommand(api, db),
                             new ScheduleCommand(api),
                             new SeasonCommand(api))
                .build();

        new JDABuilder(AccountType.BOT)
                .setToken(config.getString("bot-token"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.playing("Loading..."))
                .addEventListeners(client)
                .build();
    }
}
