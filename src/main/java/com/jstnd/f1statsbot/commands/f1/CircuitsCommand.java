package com.jstnd.f1statsbot.commands.f1;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jstnd.f1statsbot.database.Database;
import com.jstnd.f1statsbot.util.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CircuitsCommand extends Command {

    private Database db;

    public CircuitsCommand(Database db) {
        this.name = "circuits";
        this.guildOnly = false;
        this.db = db;
    }

    @Override
    protected void execute(CommandEvent event) {
        int pageNum = 1;

        if (!event.getArgs().isEmpty()) {
            if (event.getArgs().matches("^([1-3])$")) {
                pageNum = Integer.parseInt(event.getArgs());
            } else {
                event.replyError("Invalid command use. Please see `.f1 help circuits` for valid command usage.");
                return;
            }
        }

        int beginId = ((pageNum - 1) * Constants.ITEMS_PER_PAGE) + 1;
        int endId = pageNum * Constants.ITEMS_PER_PAGE;

        ResultSet rs = db.getCircuitList(beginId, endId);

        StringBuilder circuitNames = new StringBuilder();
        StringBuilder circuitIds = new StringBuilder();

        try {
            while (rs.next()) {
                circuitNames.append(rs.getString("name")).append("\n");
                circuitIds.append(rs.getString("circuit_id")).append("\n");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        event.reply(new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setColor(Constants.RED)
                        .setTitle(Constants.FLAG + " Formula One Circuits " + Constants.FLAG)
                        .addField("Circuit", circuitNames.toString(), true)
                        .addField("Circuit ID", circuitIds.toString(), true)
                        .setFooter("Page " + pageNum + " of 3")
                        .build())
                .build());
    }

    @Override
    public String getHelp() {
        return "```Usage:\n" +
                "\t.f1 circuits [page_number]\n\n" +
                "\tpage_number (optional): specifies which page of circuits to display.\n" +
                "\t                        displays page 1 if omitted.\n\n" +
                "Description:\n" +
                "\tDisplays list of circuits and their id's for reference purposes.\n\n" +
                "Pages:\n" +
                "\tCircuits are listed in alphabetical order by their circuit id's.\n" +
                "\t1: Adelaide Street Circuit - Hanoi Street Circuit\n" +
                "\t2: Hockenheimring - Circuit de Pedralbes\n" +
                "\t3: Pescara Circuit - Zolder\n\n" +
                "Example:\n" +
                "\t'.f1 circuits 2' - Displays page 2 of the list of circuits.```";
    }
}
