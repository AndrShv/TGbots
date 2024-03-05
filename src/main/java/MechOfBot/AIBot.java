package MechOfBot;
import org.json.JSONException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class AIBot extends TelegramLongPollingBot{
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            Long chat_id = update.getMessage().getChatId();
            if (message_text.startsWith("/ai")){
                String query = message_text.substring(3).trim();
                String response = getAIResponse(query);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chat_id);
                sendMessage.setText(response);
                try {
                    execute(sendMessage);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }

            }
        }
    }

    private String getAIResponse(String query) {
        try {
            HttpURLConnection con = OpenAIAPIRequest.getHttpURLConnection(query);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine).append("\n");
                }
                JSONObject jsonResponse = new JSONObject(response.toString());

                JSONArray choices = jsonResponse.getJSONArray("choices");
                JSONObject firstChoice = choices.getJSONObject(0);
                JSONObject message = firstChoice.getJSONObject("message");
                return message.getString("content");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return "Failed to get AI response.";
        }
    }

    @Override
    public String getBotUsername() {
        return "Bob1ksGavvvBot";
    }
    @Override
    public String getBotToken() {
        return "6543831910:AAH1b3d8hV3O5xoQ_aR6O61eOZWLiFWIbEk";
    }


}
