package com.jstnd.f1statsbot.commands.f1;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jstnd.f1statsbot.database.Database;
import com.jstnd.f1statsbot.ergastapi.ErgastApi;
import com.jstnd.f1statsbot.util.Constants;
import com.jstnd.f1statsbot.util.GeneralUtils;
import com.jstnd.f1statsbot.util.Table;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QualifyingCommand extends Command {

    private ErgastApi api;
    private Database db;

    public QualifyingCommand(ErgastApi api, Database db) {
        this.name = "qualifying";
        this.aliases = new String[]{"quali"};
        this.guildOnly = false;
        this.api = api;
        this.db = db;
    }

    @Override
    protected void execute(CommandEvent event) {
        String year = "current";
        String round = "last";

        if (!event.getArgs().isEmpty()) {
            String[] args = event.getArgs().split("\\s+");
            if (GeneralUtils.isValidRace(args)) {
                year = args[0];
                round = args[1];
            } else {
                event.replyError("Invalid command use. Please see `.f1 help qualifying` for valid command usage.");
                return;
            }
        }

        JSONObject race;
        try {
            race = new JSONObject(api.getQualifyingResults(year, round)).getJSONObject("MRData").getJSONObject("RaceTable").getJSONArray("Races").getJSONObject(0);
        } catch (JSONException e) {
            event.replyError("A race was not found for the given season year and round number.");
            return;
        }

        JSONObject circuit = race.getJSONObject("Circuit");
        JSONObject location = circuit.getJSONObject("Location");

        event.reply(GeneralUtils.getRaceEmbedMessage(db, race, circuit, location));

        printQualifyingResults(event, race);
    }

    private void printQualifyingResults(CommandEvent event, JSONObject race) {
        Table qualifyingTable = new Table();
        qualifyingTable.setHeaders("Pos", "Driver", "Constructor", "Q1", "Q2", "Q3");

        JSONArray qualifyingResults = race.getJSONArray("QualifyingResults");

        for (int i = 0; i < qualifyingResults.length(); ++i) {
            JSONObject standing = qualifyingResults.getJSONObject(i);
            JSONObject driver = standing.getJSONObject("Driver");
            JSONObject constructor = standing.getJSONObject("Constructor");

            String position = standing.getString("position");
            String driverName = driver.getString("givenName") + " " + driver.getString("familyName");
            String constructorName = constructor.getString("name");

            String q1Time = standing.optString("Q1");
            String q2Time = standing.optString("Q2");
            String q3Time = standing.optString("Q3");

            qualifyingTable.addRow(position, driverName, constructorName, q1Time, q2Time, q3Time);
        }

        event.reply(Constants.FLAG + " Qualifying Results " + Constants.FLAG);
        event.reply("```" + qualifyingTable.render() + "```");
    }

    @Override
    public String getHelp() {
        return "```Usage:\n" +
                "\t.f1 qualifying [<season> <round>]\n" +
                "\t.f1 quali      [<season> <round>]\n\n" +
                "\tseason & round (optional): season specifies desired F1 season.\n" +
                "\t                           round specifies desired race from given F1 season.\n" +
                "\t                           returns last race if omitted.\n\n" +
                "Description:\n" +
                "\tDisplays qualifying results for an F1 race.\n\n" +
                "Examples:\n" +
                "\t'.f1 quali 2008 7' - Displays qualifying results for the 7th race of the 2008 season.\n" +
                "\t'.f1 quali'        - Displays qualifying results for the previous race.```";
    }
}
