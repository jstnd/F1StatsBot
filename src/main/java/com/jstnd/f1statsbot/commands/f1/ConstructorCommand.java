package com.jstnd.f1statsbot.commands.f1;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jstnd.f1statsbot.util.Constants;
import com.jstnd.f1statsbot.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConstructorCommand extends Command {

    private Database db;

    public ConstructorCommand(Database db) {
        this.name = "constructor";
        this.aliases = new String[]{"team"};
        this.guildOnly = false;
        this.db = db;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split("\\s+");

        if (args.length != 1 || args[0].isEmpty()) {
            event.replyError("Invalid command use. Please see `.f1 help constructor` for valid command usage.");
            return;
        }

        ResultSet rs = db.getConstructorById(args[0]);

        EmbedBuilder eb = null;

        try {
            while (rs.next()) {
                eb = new EmbedBuilder()
                        .setColor(Constants.RED)
                        .setTitle(rs.getString("name"), rs.getString("wiki_url"))
                        .setThumbnail(rs.getString("logo_url"))
                        .addField("Nationality", rs.getString("nationality"), true)
                        .addField("Active Seasons", !rs.getString("active_seasons").isEmpty() ? rs.getString("active_seasons") : "N/A", true)
                        .addField("Best WCC Position", rs.getInt("best_wcc_pos") != 404 ? String.valueOf(rs.getInt("best_wcc_pos")) : "N/A", true)
                        .addField("Constructors' Championships", rs.getString("wcc_wins"), false)
                        .addField("Drivers' Championships", rs.getString("wdc_wins"), false)
                        .addField("Race Entries", String.valueOf(rs.getInt("race_entries")), true)
                        .addField("Race Wins", String.valueOf(rs.getInt("race_wins")), true)
                        .addField("Points", String.valueOf(rs.getDouble("points")), true)
                        .addField("Poles", String.valueOf(rs.getInt("pole_positions")), true)
                        .addField("Podiums", String.valueOf(rs.getInt("race_podiums")), true)
                        .addField("Fastest Laps", String.valueOf(rs.getInt("fastest_laps")), true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (eb == null) {
            event.replyError("Invalid constructor id. Please see `.f1 help constructors` for a list of valid constructor id's.");
            return;
        }

        event.reply(new MessageBuilder()
                .setEmbed(eb.build())
                .build());
    }

    @Override
    public String getHelp() {
        return "```Usage:\n" +
                "\t.f1 constructor <constructor_id>\n" +
                "\t.f1 team        <constructor_id>\n\n" +
                "\tconstructor_id (required): id used by the Ergast API to represent a specific constructor.\n" +
                "\tSee the '.f1 constructors' command for the id's representing each constructor.\n\n" +
                "Description:\n" +
                "\tDisplays various information about a specified constructor.\n\n" +
                "Examples:\n" +
                "\t'.f1 constructor ferrari'  - Displays information about the constructor Ferrari.\n" +
                "\t'.f1 constructor red_bull' - Displays information about the constructor Red Bull.```";
    }
}
