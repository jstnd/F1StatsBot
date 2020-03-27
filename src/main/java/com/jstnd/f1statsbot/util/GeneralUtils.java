package com.jstnd.f1statsbot.util;

import com.jstnd.f1statsbot.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Year;

public class GeneralUtils {

    public static String getJsonString(String url) {
        StringBuilder sb = new StringBuilder();

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "F1StatsBot/1.0");

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    // ================ Various API Methods ================

    public static String getCountryCode(String country) {
        String url = "https://restcountries.eu/rest/v2/name/{COUNTRY}".replace("{COUNTRY}", country);
        String json = getJsonString(url);

        return new JSONArray(json).getJSONObject(0).getString("alpha3Code");
    }

    // ================ Input Validation Methods ================

    public static boolean isValidRace(String[] inputs) {
        return (inputs.length == 2) && isValidSeason(inputs[0]) && inputs[1].matches("^([1-9]|1\\d|2[0-2])$");
    }

    public static boolean isValidSeason(String input) {
        return input.matches("^\\d{4}$")
                && (Integer.parseInt(input) >= 1950) && (Integer.parseInt(input) <= Year.now().getValue());
    }

    // ================ Universal Message Creation Methods ================

    public static Message getRaceEmbedMessage(Database db, JSONObject race, JSONObject circuit, JSONObject location) {
        return new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setColor(Constants.RED)
                        .setTitle(race.getString("season") + " " + race.getString("raceName"), race.getString("url"))
                        .setThumbnail(db.getCircuitThumbnail(circuit.getString("circuitId")))
                        .addField("Circuit", "[" + circuit.getString("circuitName") + "](" + circuit.getString("url") + ")", true)
                        .addField("Location", location.getString("locality") + ", " + location.getString("country"), true)
                        .addField("", "", true)
                        .addField("Date", race.getString("date"), true)
                        .addField("Time", race.getString("time").replace(":00Z", " UTC"), true)
                        .addField("", "", true)
                        .build())
                .build();
    }

}
