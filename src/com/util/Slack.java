package com.util;

import com.config.Config;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;


class SlackRequest {
    private String channel;
    private String username;
    private String text;
    private String icon_emoji;

    public SlackRequest(String channel, String username, String text, String icon_emoji) {
        this.channel = channel;
        this.username = username;
        this.text = text;
        this.icon_emoji = icon_emoji;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon_emoji() {
        return icon_emoji;
    }

    public void setIcon_emoji(String icon_emoji) {
        this.icon_emoji = icon_emoji;
    }
}

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
            Utils.println("test...");
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
