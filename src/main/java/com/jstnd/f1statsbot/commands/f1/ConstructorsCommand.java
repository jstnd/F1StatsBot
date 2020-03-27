package com.jstnd.f1statsbot.commands.f1;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jstnd.f1statsbot.database.Database;
import com.jstnd.f1statsbot.util.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConstructorsCommand extends Command {

    private Database db;

    public ConstructorsCommand(Database db) {
        this.name = "constructors";
        this.aliases = new String[]{"teams"};
        this.guildOnly = false;
        this.db = db;
    }

    @Override
    protected void execute(CommandEvent event) {
        int pageNum = 1;

        if (!event.getArgs().isEmpty()) {
            if (event.getArgs().matches("^([1-9])$")) {
                pageNum = Integer.parseInt(event.getArgs());
            } else {
                event.replyError("Invalid command use. Please see `.f1 help constructors` for valid command usage.");
                return;
            }
        }

        int beginId = ((pageNum - 1) * Constants.ITEMS_PER_PAGE) + 1;
        int endId = pageNum * Constants.ITEMS_PER_PAGE;

        ResultSet rs = db.getConstructorList(beginId, endId);

        StringBuilder constructorNames = new StringBuilder();
        StringBuilder constructorIds = new StringBuilder();

        try {
            while (rs.next()) {
                constructorNames.append(rs.getString("name")).append("\n");
                constructorIds.append(rs.getString("constructor_id")).append("\n");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        event.reply(new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setColor(Constants.RED)
                        .setTitle(Constants.FLAG + " Formula One Constructors " + Constants.FLAG)
                        .addField("Constructor", constructorNames.toString(), true)
                        .addField("Constructor ID", constructorIds.toString(), true)
                        .setFooter("Page " + pageNum + " of 9")
                        .build())
                .build());
    }

    @Override
    public String getHelp() {
        return "```Usage:\n" +
                "\t.f1 constructors [page_number]\n" +
                "\t.f1 teams        [page_number]\n\n" +
                "\tpage_number (optional): specifies which page of constructors to display.\n" +
                "\t                        displays page 1 if omitted.\n\n" +
                "Description:\n" +
                "\tDisplays list of constructors and their id's for reference purposes.\n\n" +
                "Pages:\n" +
                "\tConstructors are listed in alphabetical order.\n" +
                "\t1: Adams - Brabham-Ford\t   6: Martini - Pacific\n" +
                "\t2: Brabham-Repco - Dallara\t7: Pankratz - Shadow-Matra\n" +
                "\t3: De Tomaso - Fondmetal\t  8: Shannon - Trojan\n" +
                "\t4: Footwork - Langley\t     9: Turner - Zakspeed\n" +
                "\t5: Larrousse - Marchese\n\n" +
                "Example:\n" +
                "\t'.f1 constructors 4' - Displays page 4 of the list of constructors.```";
    }
}
