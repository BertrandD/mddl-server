package com.middlewar.api.util.slack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.middlewar.core.config.Config;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Bertrand
 */
public class Slack {

    public static void sendWarning(String message)
    {
        try {
            SlackRequest request = new SlackRequest(Config.SLACK_WARNING_CHANNEL, Config.SLACK_WARNING_BOT_NAME, message, Config.SLACK_WARNING_BOT_ICON);

            ObjectMapper mapper = new ObjectMapper();

            String str = mapper.writeValueAsString(request);
            excutePost(Config.SLACK_URL, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendInfo(String message)
    {
        try {
            SlackRequest request = new SlackRequest(Config.SLACK_INFO_CHANNEL, Config.SLACK_INFO_BOT_NAME, message, Config.SLACK_INFO_BOT_ICON);

            ObjectMapper mapper = new ObjectMapper();

            String str = mapper.writeValueAsString(request);
            excutePost(Config.SLACK_URL, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String excutePost(String targetURL, String urlParameters)
    {
        URL url;
        HttpsURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
