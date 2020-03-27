package com.jstnd.f1statsbot.commands.f1;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jstnd.f1statsbot.ergastapi.ErgastApi;
import com.jstnd.f1statsbot.util.Constants;
import com.jstnd.f1statsbot.util.GeneralUtils;
import com.jstnd.f1statsbot.util.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class SeasonCommand extends Command {

    private ErgastApi api;

    public SeasonCommand(ErgastApi api) {
        this.name = "season";
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
                event.replyError("Invalid command use. Please see `.f1 help season` for valid command usage.");
                return;
            }
        }

        // This is located here rather than in the method below for the purpose of creating the embed
        JSONObject driversData = new JSONObject(api.getSeasonDriverStandings(year));
        String season = driversData.getJSONObject("MRData").getJSONObject("StandingsTable").getString("season");

        event.reply(new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setColor(Constants.RED)
                        .setTitle(season + " Formula One World Championship", "https://en.wikipedia.org/wiki/{SEASON}_Formula_One_season".replace("{SEASON}", season))
                        .build())
                .build());

        // This if statement is used since constructor standings are only available since the 1958 season
        if (Integer.parseInt(season) >= 1958) {
            printConstructorStandings(event, season);
        }

        printDriverStandings(event, driversData);
    }

    private void printConstructorStandings(CommandEvent event, String year) {
        Table constructorsTable = new Table();
        constructorsTable.setHeaders("Pos", "Constructor", "Points", "Wins");

        JSONObject obj = new JSONObject(api.getSeasonConstructorStandings(year));
        JSONArray constructorStandings = obj.getJSONObject("MRData").getJSONObject("StandingsTable").getJSONArray("StandingsLists").getJSONObject(0).getJSONArray("ConstructorStandings");

        for (int i = 0; i < constructorStandings.length(); ++i) {
            JSONObject standing = constructorStandings.getJSONObject(i);
            JSONObject constructor = standing.getJSONObject("Constructor");

            String position = standing.getString("positionText");
            String points = standing.getString("points");
            String raceWins = standing.getString("wins");
            String constructorName = constructor.getString("name");

            constructorsTable.addRow(position, constructorName, points, raceWins);
        }

        event.reply(Constants.FLAG + " Constructor Standings " + Constants.FLAG);
        event.reply("```" + constructorsTable.render() + "```");
    }

    private void printDriverStandings(CommandEvent event, JSONObject obj) {
        Table driversTable = new Table();
        driversTable.setHeaders("Pos", "Driver", "Constructor(s)", "Points", "Wins");

        JSONArray driverStandings = obj.getJSONObject("MRData").getJSONObject("StandingsTable").getJSONArray("StandingsLists").getJSONObject(0).getJSONArray("DriverStandings");

        for (int i = 0; i < driverStandings.length(); ++i) {
            JSONObject standing = driverStandings.getJSONObject(i);
            JSONObject driver = standing.getJSONObject("Driver");
            JSONArray constructors = standing.getJSONArray("Constructors");

            String position = standing.getString("positionText");
            String points = standing.getString("points");
            String raceWins = standing.getString("wins");
            String driverName = driver.getString("givenName") + " " + driver.getString("familyName");

            StringBuilder constructorNames = new StringBuilder();
            for (int j = 0; j < constructors.length(); ++j) {
                JSONObject constructor = constructors.getJSONObject(j);
                constructorNames.append(constructor.getString("name"));
                if (j != constructors.length() - 1) {
                    constructorNames.append("/");
                }
            }

            driversTable.addRow(position, driverName, constructorNames.toString(), points, raceWins);
        }

        event.reply(Constants.FLAG + " Driver Standings " + Constants.FLAG);
        event.reply("```" + driversTable.render() + "```");
    }

    @Override
    public String getHelp() {
        return "```Usage:\n" +
                "\t.f1 season [season]\n" +
                "\tseason (optional): specifies desired F1 season.\n" +
                "\t                   displays standings of current season if omitted.\n\n" +
                "Description:\n" +
                "\tDisplays constructor and driver standings of an F1 season.\n\n" +
                "Examples:\n" +
                "\t'.f1 season 2014' - Displays constructor and driver standings of the 2014 season.\n" +
                "\t'.f1 season'      - Displays constructor and driver standings of the current season.```";
    }
}
