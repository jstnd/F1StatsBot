package com.jstnd.f1statsbot.commands.f1;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jstnd.f1statsbot.database.Database;
import com.jstnd.f1statsbot.util.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverCommand extends Command {

    private Database db;

    public DriverCommand(Database db) {
        this.name = "driver";
        this.guildOnly = false;
        this.db = db;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split("\\s+");

        if (args.length != 1 || args[0].isEmpty()) {
            event.replyError("Invalid command use. Please see `.f1 help driver` for valid command usage.");
            return;
        }

        ResultSet rs = db.getDriverById(args[0]);

        EmbedBuilder eb = null;

        try {
            while (rs.next()) {
                eb = new EmbedBuilder()
                        .setColor(Constants.RED)
                        .setTitle(rs.getString("name"), rs.getString("wiki_url"))
                        .setThumbnail(rs.getString("picture_url"))
                        .addField("Nationality", rs.getString("nationality"), true)
                        .addField("Born", rs.getString("dob"), true)
                        .addField(!rs.getString("dod").isEmpty() ? "Died" : "", rs.getString("dod"), true)
                        .addField("Active Seasons", rs.getString("active_seasons"), true)
                        .addField("Best WDC Position", rs.getInt("best_wdc_pos") != 404 ? "" + rs.getInt("best_wdc_pos") : "N/A", true)
                        .addField(!rs.getString("perm_number").isEmpty() ? "Permanent Number" : "", rs.getString("perm_number"), true)
                        .addField("Teams", rs.getString("teams"), true)
                        .addField("Drivers' Championships", rs.getString("wdc_wins"), true)
                        .addField("", "", true)
                        .addField("Race Entries", "" + rs.getInt("race_entries"), true)
                        .addField("Race Wins", "" + rs.getInt("race_wins"), true)
                        .addField("Points", "" + rs.getDouble("points"), true)
                        .addField("Poles", "" + rs.getInt("pole_positions"), true)
                        .addField("Podiums", "" + rs.getInt("race_podiums"), true)
                        .addField("Fastest Laps", "" + rs.getInt("fastest_laps"), true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (eb == null) {
            event.replyError("Invalid driver id. Please see `.f1 help drivers` for a list of valid driver id's.");
            return;
        }

        event.reply(new MessageBuilder()
                .setEmbed(eb.build())
                .build());
    }

    @Override
    public String getHelp() {
        return "```Usage:\n" +
                "\t.f1 driver <driver_id>\n\n" +
                "\tdriver_id (required): id used by the Ergast API to represent a specific driver.\n" +
                "\tSee the '.f1 drivers' command for the id's representing each driver.\n\n" +
                "Description:\n" +
                "\tDisplays information about a specified driver.\n\n" +
                "Examples:\n" +
                "\t'.f1 driver vettel' - Displays information about the driver Sebastian Vettel.\n" +
                "\t'.f1 driver albon'  - Displays information about the driver Alexander Albon.```";
    }
}
