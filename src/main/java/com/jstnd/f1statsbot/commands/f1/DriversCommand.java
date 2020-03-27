package com.jstnd.f1statsbot.commands.f1;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jstnd.f1statsbot.util.Constants;
import com.jstnd.f1statsbot.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DriversCommand extends Command {

    private Database db;

    public DriversCommand(Database db) {
        this.name = "drivers";
        this.guildOnly = false;
        this.db = db;
    }

    @Override
    protected void execute(CommandEvent event) {
        int pageNum = 1;

        if (!event.getArgs().isEmpty()) {
            if (event.getArgs().matches("^([1-9]|[1-2]\\d|3[0-4])$")) {
                pageNum = Integer.parseInt(event.getArgs());
            } else {
                event.replyError("Invalid command use. Please see `.f1 help drivers` for valid command usage.");
                return;
            }
        }

        int beginId = ((pageNum - 1) * Constants.ITEMS_PER_PAGE) + 1;
        int endId = pageNum * Constants.ITEMS_PER_PAGE;

        ResultSet rs = db.getDriverList(beginId, endId);

        StringBuilder driverNames = new StringBuilder();
        StringBuilder driverIds = new StringBuilder();

        try {
            while (rs.next()) {
                driverNames.append(rs.getString("name")).append("\n");
                driverIds.append(rs.getString("driver_id")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        event.reply(new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setColor(Constants.RED)
                        .setTitle(Constants.FLAG + " Formula One Drivers " + Constants.FLAG)
                        .addField("Driver", driverNames.toString(), true)
                        .addField("Driver ID", driverIds.toString(), true)
                        .setFooter("Page " + pageNum + " of 34")
                        .build())
                .build());
    }

    @Override
    public String getHelp() {
        return "```Usage:\n" +
                "\t.f1 drivers [page_number]\n\n" +
                "\tpage_number (optional): specifies which page of drivers to display.\n" +
                "\t                        displays page 1 if omitted.\n\n" +
                "Description:\n" +
                "\tDisplays list of drivers and their id's for reference purposes.\n\n" +
                "Pages:\n" +
                "\tDrivers are listed in alphabetical order by last name.\n" +
                "\t1:  Carlo Abate - Keith Andrews\t        18: Helmuth Koinigg - Chris Lawrence\n" +
                "\t2:  Marco Apicella - Rubens Barrichello\t19: Charles Leclerc - Jean Lucienbonnet\n" +
                "\t3:  Michael Bartels - Jules Bianchi\t    20: Brett Lunger - Eugène Martin\n" +
                "\t4:  Gino Bianco - Johnny Boyd\t          21: Pierluigi Martini - André Milhoux\n" +
                "\t5:  David Brabham - Ivor Bueb\t          22: Chet Miller - Felipe Nasr\n" +
                "\t6:  Sébastien Buemi - Jay Chamberlain\t  23: Massimo Natili - Torsten Palm\n" +
                "\t7:  Karun Chandhok - David Coulthard\t   24: Jonathan Palmer - Ernie Pieterse\n" +
                "\t8:  Piers Courage - Andrea de Adamich\t  25: Paul Pietsch - Ian Raby\n" +
                "\t9:  Elio de Angelis - Martin Donnelly\t  26: Bobby Rahal - Pedro Rodríguez\n" +
                "\t10: Mark Donohue - Teo Fabi\t            27: Ricardo Rodríguez - Roy Salvadori\n" +
                "\t11: Corrado Fabi - Norberto Fontana\t    28: Consalvo Sanesi - Piero Scotti\n" +
                "\t12: Azdrubal Fontes - Jo Gartner\t       29: Wolfgang Seidel - Mike Spence\n" +
                "\t13: Pierre Gasly - Keith Greene\t        30: Alan Stacey - Noritake Takahara\n" +
                "\t14: Masten Gregory - Cuth Harrison\t     31: Kunimitsu Takahashi - Jarno Trulli\n" +
                "\t15: Brian Hart - Ingo Hoffmann\t         32: Esteban Tuero - Ottorino Volonterio\n" +
                "\t16: Bill Holland - Stefan Johansson\t    33: Rikky von Opel - Graham Whitehead\n" +
                "\t17: Eddie Johnson - Kamui Kobayashi\t    34: Bill Whitehouse - Ricardo Zunino\n\n" +
                "Example:\n" +
                "\t'.f1 drivers 19' - Displays page 19 of the list of drivers.```";
    }
}
