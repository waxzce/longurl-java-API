/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.waxzce.longurl;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author waxzce
 */
public class LongUrl {

    private String shorturl;
    private JsonElement jsonresponse = null;

    public String getLongUrl() {
        String returnvalue = "";
        if (jsonresponse == null) {
            this.getData();
        }
        returnvalue = jsonresponse.getAsJsonObject().get("long-url").getAsString();
        return returnvalue;
    }

    public String getTitle() {
        String returnvalue = "";
        if (jsonresponse == null) {
            this.getData();
        }
        if (jsonresponse.getAsJsonObject().get("title") == null) {
            try {
                URL url = new URL(this.getLongUrl());
                returnvalue = url.getFile();
            } catch (MalformedURLException ex) {
                Logger.getLogger(LongUrl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            returnvalue = jsonresponse.getAsJsonObject().get("title").getAsString();
        }
        return returnvalue;
    }

    private void getData() {

        try {
            URL url;

            url = new URL("http://api.longurl.org/v2/expand?format=json&title=1&url=" + shorturl);

            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            String full = "";
            while ((line = in.readLine()) != null) {
                full = full + line;
            }
            //out.print(full);

            JsonParser jp = new JsonParser();
            jsonresponse = jp.parse(full);


        } catch (MalformedURLException ex) {
            Logger.getLogger(LongUrl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LongUrl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getShorturl() {
        return shorturl;
    }

    public void setShorturl(String shorturl) {
        this.shorturl = shorturl;
    }

    public LongUrl(String shorturl) {
        this.shorturl = shorturl;
    }
}
