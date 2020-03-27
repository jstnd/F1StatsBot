package com.jstnd.f1statsbot.commands.f1;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jstnd.f1statsbot.ergastapi.ErgastApi;
import com.jstnd.f1statsbot.util.Constants;
import com.jstnd.f1statsbot.util.GeneralUtils;
import com.jstnd.f1statsbot.util.Table;
import com.jstnd.f1statsbot.database.Database;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RaceCommand extends Command {

    private ErgastApi api;
    private Database db;

    public RaceCommand(ErgastApi api, Database db) {
        this.name = "race";
        this.guildOnly = false;
        this.api = api;
        this.db = db;
    }

    @Override
    protected void execute(CommandEvent event) {
        String year = "current";
        String round = "last";

        if (!event.getArgs().isEmpty() && !event.getArgs().equals("last")) {
            String[] args = event.getArgs().split("\\s+");
            if (GeneralUtils.isValidRace(args)) {
                year = args[0];
                round = args[1];
            } else {
                event.replyError("Invalid command use. Please see `.f1 help race` for valid command usage.");
                return;
            }
        }

        JSONObject race;
        try {
            race = new JSONObject(api.getRaceResults(year, round)).getJSONObject("MRData").getJSONObject("RaceTable").getJSONArray("Races").getJSONObject(0);
        } catch (JSONException e) {
            event.replyError("A race was not found for the given season year and round number.");
            return;
        }

        JSONObject circuit = race.getJSONObject("Circuit");
        JSONObject location = circuit.getJSONObject("Location");

        event.reply(GeneralUtils.getRaceEmbedMessage(db, race, circuit, location));

        printRaceResults(event, race);
    }

    private void printRaceResults(CommandEvent event, JSONObject race) {
        Table resultsTable = new Table();
        resultsTable.setHeaders("Pos", "Driver", "Constructor", "Laps", "Grid", "Time", "Status", "Points");

        JSONArray raceResults = race.getJSONArray("Results");

        for (int i = 0; i < raceResults.length(); ++i) {
            JSONObject result = raceResults.getJSONObject(i);
            JSONObject driver = result.getJSONObject("Driver");
            JSONObject constructor = result.getJSONObject("Constructor");

            String position = result.getString("positionText");
            String points = result.getString("points");
            String gridPosition = result.getString("grid");
            String lapsCompleted = result.getString("laps");
            String finishStatus = result.getString("status");
            String driverName = driver.getString("givenName") + " " + driver.getString("familyName");
            String constructorName = constructor.getString("name");
            String raceTime;
            try {
                raceTime = result.getJSONObject("Time").getString("time");
            } catch(JSONException e) {
                raceTime = "";
            }

            resultsTable.addRow(position, driverName, constructorName, lapsCompleted, gridPosition, raceTime, finishStatus, points);
        }

        event.reply(Constants.FLAG + " Race Results " + Constants.FLAG);
        event.reply("```" + resultsTable.render() + "```");
    }

    @Override
    public String getHelp() {
        return "```Usage:\n" +
                "\t.f1 race [<season> <round>]\n\n" +
                "\tseason & round (optional): season specifies desired F1 season.\n" +
                "\t                           round specifies desired race from given F1 season.\n" +
                "\t                           returns last race if omitted.\n\n" +
                "Description:\n" +
                "\tDisplays driver results of an F1 race.\n\n" +
                "Examples:\n" +
                "\t'.f1 race 2008 4' - Displays driver results for the 4th race of the 2008 season.\n" +
                "\t'.f1 race'        - Displays driver results for the previous race.```";
    }
}
