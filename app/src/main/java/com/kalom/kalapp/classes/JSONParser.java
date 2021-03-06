package com.kalom.kalapp.classes;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class JSONParser {
    

    public String getKey(String URL,String Key) throws IOException, JSONException{

        JSONParser jsr = new JSONParser();

        JSONObject json =     jsr.readJson(URL);
       // System.out.println(json.getString("zip_code"));

        return (String) json.get(Key);
    }


    public JSONObject readJson(String url) throws JSONException, IOException {

        URL ur = new URL(url);

        // given a url open a connection
        URLConnection c = ur.openConnection();

        // set the connection timeout to 5 seconds
        c.setConnectTimeout(15*1000);
        c.setReadTimeout(15*1000);
        c.setUseCaches(false);


        InputStream is = c.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
        BufferedReader rd = new BufferedReader(isr);
        String jsonText = readAll(rd);
        JSONObject json = new JSONObject(jsonText);
        is.close();

        return json;


    }


    public String JsonString(String url) throws IOException {
        InputStream is = new URL (url).openStream();
        InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
        BufferedReader rd = new BufferedReader(isr);
        String jsonText = readAll(rd);
        is.close();
        return jsonText;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }




}