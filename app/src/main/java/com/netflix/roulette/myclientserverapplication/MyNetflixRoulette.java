package com.netflix.roulette.myclientserverapplication;

import com.net.codeusa.NetflixRoulette;
import com.net.codeusa.RouletteFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

class MyNetflixRoulette {

    final private String searchByTitleURL = "http://netflixroulette.net/api/api.php?title=";
    final private String searchByDirectorURL = "http://netflixroulette.net/api/api.php?director=";

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();

        int cp;
        while((cp = rd.read()) != -1) {
            sb.append((char)cp);
        }

        return sb.toString();
    }

    private JSONArray readJsonFromUrl(String url) throws IOException, JSONException {

        JSONArray var6;
        try (InputStream is = (new URL(url)).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);

            if (jsonText.charAt(0) != '[') jsonText = "[" + jsonText + "]";

            var6 = new JSONArray(jsonText);
        }

        return var6;
    }

    private JSONArray getData(String data, String URL) throws JSONException, IOException {
        int year = 0;
        data = data.replace(" ", "%20");
        return readJsonFromUrl(URL + data + "&year=" + year);
    }

    JSONArray getMovieByDirector(String data) throws JSONException, IOException {
        return getData(data, searchByDirectorURL);
    }

    JSONArray getMovieByTitle(String data) throws JSONException, IOException {
        return getData(data, searchByTitleURL);
    }

}
