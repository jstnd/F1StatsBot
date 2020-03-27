package com.jstnd.f1statsbot.commands.f1;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jstnd.f1statsbot.util.GeneralUtils;
import com.jstnd.f1statsbot.database.Database;
import com.jstnd.f1statsbot.ergastapi.ErgastApi;
import org.json.JSONObject;

public class NextCommand extends Command {

    private ErgastApi api;
    private Database db;

    public NextCommand(ErgastApi api, Database db) {
        this.name = "next";
        this.guildOnly = false;
        this.api = api;
        this.db = db;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!event.getArgs().isEmpty()) {
            event.replyError("Invalid command use; no arguments are required for the `.f1 next` command.");
            return;
        }

        JSONObject obj = new JSONObject(api.getNextRace());
        JSONObject race = obj.getJSONObject("MRData").getJSONObject("RaceTable").getJSONArray("Races").getJSONObject(0);
        JSONObject circuit = race.getJSONObject("Circuit");
        JSONObject location = circuit.getJSONObject("Location");

        event.reply(GeneralUtils.getRaceEmbedMessage(db, race, circuit, location));
    }

    @Override
    public String getHelp() {
        return "```Usage:\n" +
                "\t.f1 next\n\n" +
                "Description:\n" +
                "\tDisplays information about the upcoming F1 race.```";
    }
}
