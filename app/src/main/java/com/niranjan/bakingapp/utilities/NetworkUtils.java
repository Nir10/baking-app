package com.niranjan.bakingapp.utilities;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with baking api.
 */

public class NetworkUtils {

    final public static String
            BAKING_APP_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/" +
            "59121517_baking/baking.json";


    /**
     * Builds the URL used to fetch recipes from server.
     * @return The URL to use to fetch recipes from server.
     */
    public static URL buildRecipeURL(){

        URL url = null;

        try {
            url = new URL(BAKING_APP_URL);
        } catch(MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }




    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
