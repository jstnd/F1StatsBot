package com.jstnd.f1statsbot.commands.f1;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jstnd.f1statsbot.util.GeneralUtils;
import com.jstnd.f1statsbot.ergastapi.ErgastApi;
import com.jstnd.f1statsbot.util.Constants;
import com.jstnd.f1statsbot.util.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class ScheduleCommand extends Command {

    private ErgastApi api;

    public ScheduleCommand(ErgastApi api) {
        this.name = "schedule";
        this.guildOnly = false;
        this.api = api;
    }

    @Override
    protected void execute(CommandEvent event) {
        String year = "current";

        if (!event.getArgs().isEmpty()) {
            if (GeneralUtils.isValidSeason(event.getArgs())) {
                year = event.getArgs();
            } else {
                event.replyError("Invalid command use. Please see `.f1 help schedule` for valid command usage.");
                return;
            }
        }

        // This is here for the purpose of creating the embed
        JSONObject scheduleData = new JSONObject(api.getSeasonSchedule(year));
        String season = scheduleData.getJSONObject("MRData").getJSONObject("RaceTable").getString("season");

        event.reply(new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setColor(Constants.RED)
                        .setTitle(season + " Formula One World Championship", "https://en.wikipedia.org/wiki/{SEASON}_Formula_One_season".replace("{SEASON}", season))
                        .build())
                .build());

        printSeasonSchedule(event, scheduleData);
    }

    private void printSeasonSchedule(CommandEvent event, JSONObject obj) {
        Table scheduleTable = new Table();
        scheduleTable.setHeaders("Round", "Race", "Circuit", "Location", "Date");

        JSONArray races = obj.getJSONObject("MRData").getJSONObject("RaceTable").getJSONArray("Races");

        for (int i = 0; i < races.length(); ++i) {
            JSONObject race = races.getJSONObject(i);
            JSONObject circuit = race.getJSONObject("Circuit");
            JSONObject location = circuit.getJSONObject("Location");

            String round = race.getString("round");
            String raceName = race.getString("raceName").replace("Grand Prix", "GP");
            String circuitName = circuit.getString("circuitName").replace("Circuit", "Crct");
            String locality = location.getString("locality");
            String date = race.getString("date");

            String country = location.getString("country");
            if (country.length() > 3) {
                country = GeneralUtils.getCountryCode(country);
            }

            scheduleTable.addRow(round, raceName, circuitName, locality + ", " + country, date);
        }

        event.reply("```" + scheduleTable.render() + "```");
    }

    @Override
    public String getHelp() {
        return "```Usage:\n" +
                "\t.f1 schedule [season]\n\n" +
                "\tseason (optional): specifies desired F1 season.\n" +
                "\t                   displays schedule of current season if omitted.\n\n" +
                "Description:\n" +
                "\tDisplays race schedule of an F1 season.\n\n" +
                "Examples:\n" +
                "\t'.f1 schedule 2008' - Displays race schedule of the 2008 season.\n" +
                "\t'.f1 schedule'      - Displays race schedule of current season.```";
    }
}
