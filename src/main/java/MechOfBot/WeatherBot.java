package MechOfBot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.sql.*;

public class WeatherBot extends TelegramLongPollingBot {

    static final String JDBC_URL = "jdbc:postgresql://127.0.0.1:5433/postgres?user=postgres&password=password";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            if (messageText.startsWith("/weather")) {
                String[] messageParts = messageText.split(" ", 2);
                if (messageParts.length == 2) {
                    String city = messageParts[1];
                    String weatherMessage = getWeather(city);
                    sendResponse(update.getMessage().getChatId().toString(), weatherMessage);
                    saveWeatherToDatabase(update.getMessage().getChatId().toString(), city, weatherMessage);
                } else {
                    sendResponse(update.getMessage().getChatId().toString(), "Вы не указали город. Используйте команду в формате /weather <город>");
                }
            }
        }
    }

    private String getWeather(String city) {
        String url = "https://ua.sinoptik.ua/погода-" + city;
        try {
            Document doc = Jsoup.connect(url).get();
            Element temperatureElement = doc.selectFirst(".temperature");
            if (temperatureElement != null) {
                return "Погода в городе " + city + ": " + temperatureElement.text();
            } else {
                return "Не удалось получить данные о погоде для указанного города.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Произошла ошибка при обработке запроса.";
        }
    }

    private void sendResponse(String chatId, String message) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(message);

        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void saveWeatherToDatabase(String chatId, String city, String weather) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS weather_requests " +
                    "(id SERIAL PRIMARY KEY, " +
                    "chat_id TEXT, city TEXT, " +
                    "weather TEXT)";
            connection.createStatement().executeUpdate(createTableSQL);

            String insertSQL = "INSERT INTO weather_requests (chat_id, city, weather) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, chatId);
            statement.setString(2, city);
            statement.setString(3, weather);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public static void main(String[] args) {
        try {
            WeatherBot bot = new WeatherBot();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
