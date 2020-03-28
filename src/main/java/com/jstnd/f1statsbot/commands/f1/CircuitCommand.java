package com.jstnd.f1statsbot.commands.f1;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jstnd.f1statsbot.database.Database;
import com.jstnd.f1statsbot.util.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CircuitCommand extends Command {

    private Database db;

    public CircuitCommand(Database db) {
        this.name = "circuit";
        this.guildOnly = false;
        this.db = db;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split("\\s+");

        if (args.length != 1 || args[0].isEmpty()) {
            event.replyError("Invalid command use. Please see `.f1 help circuit` for valid command usage.");
            return;
        }

        ResultSet rs = db.getCircuitById(args[0]);

        EmbedBuilder eb = null;

        try {
            while (rs.next()) {
                eb = new EmbedBuilder()
                        .setColor(Constants.RED)
                        .setTitle(rs.getString("name"), rs.getString("wiki_url"))
                        .setThumbnail(rs.getString("picture_url"))
                        .addField("Location", rs.getString("location"), true)
                        .addField("Active Seasons", rs.getString("active_seasons"), true)
                        .addField("", "", true)
                        .addField("Lap Record", rs.getString("lap_record"), true)
                        .addField("Length", rs.getString("length"), true)
                        .addField("Turns", String.valueOf(rs.getInt("turns")), true)
                        .addField("Top Drivers", rs.getString("top_drivers"), true)
                        .addField("Top Constructors", rs.getString("top_constructors"), true);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        if (eb == null) {
            event.replyError("Invalid circuit id. Please see `.f1 help circuits` for a list of valid circuit id's.");
            return;
        }

        event.reply(new MessageBuilder()
                .setEmbed(eb.build())
                .build());
    }

    @Override
    public String getHelp() {
        return "```Usage:\n" +
                "\t.f1 circuit <circuit_id>\n\n" +
                "\tcircuit-id (required): id used by the Ergast API to represent a specific circuit.\n" +
                "\tSee the '.f1 circuits' command for the id's representing each circuit.\n\n" +
                "Description:\n" +
                "\tDisplays various information about a specified circuit.\n\n" +
                "Examples:\n" +
                "\t'.f1 circuit albert_park' - Displays information about the Albert Park GP Circuit.\n" +
                "\t'.f1 circuit monza'       - Displays information about Autodromo Nazionale di Monza.```";
    }
}
