package com.jstnd.f1statsbot.ergastapi;

import com.jstnd.f1statsbot.util.GeneralUtils;

public class ErgastApi {

    private static final String BASE_URL = "http://ergast.com/api/f1";

    // ================ Race Methods ================

    public String getRaceResults(String season, String round) {
        String url = BASE_URL + "/{SEASON}/{ROUND}/results.json".replace("{SEASON}", season).replace("{ROUND}", round);
        return GeneralUtils.getJsonString(url);
    }

    public String getNextRace() {
        String url = BASE_URL + "/current/next.json";
        return GeneralUtils.getJsonString(url);
    }

    public String getQualifyingResults(String season, String round) {
        String url = BASE_URL + "/{SEASON}/{ROUND}/qualifying.json".replace("{SEASON}", season).replace("{ROUND}", round);
        return GeneralUtils.getJsonString(url);
    }

    // ================ Season Methods ================

    public String getSeasonDriverStandings(String season) {
        String url = BASE_URL + "/{SEASON}/driverStandings.json".replace("{SEASON}", season);
        return GeneralUtils.getJsonString(url);
    }

    public String getSeasonConstructorStandings(String season) {
        String url = BASE_URL + "/{SEASON}/constructorStandings.json".replace("{SEASON}", season);
        return GeneralUtils.getJsonString(url);
    }

    public String getSeasonSchedule(String season) {
        String url = BASE_URL + "/{SEASON}.json".replace("{SEASON}", season);
        return GeneralUtils.getJsonString(url);
    }

}
