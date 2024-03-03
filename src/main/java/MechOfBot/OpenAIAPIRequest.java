package MechOfBot;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class OpenAIAPIRequest {

    public static HttpURLConnection getHttpURLConnection(String query) throws IOException, URISyntaxException {
        String url = "https://api.openai.com/v1/chat/completions";
        URL obj = new URI(url).toURL();
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer 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");

        con.setDoOutput(true);

        String requestBody = "{ \"model\": \"gpt-3.5-turbo\", " +
                "\"messages\": [{\"role\": \"user\", \"content\": \"" + query + "\"}], " +
                "\"temperature\": 0.7 }";

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(requestBody);
            wr.flush();
        }
        return con;
    }
}

