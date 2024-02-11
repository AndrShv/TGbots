package MechOfBot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.io.IOException;

public class BobikBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            String userText = message.getText();
            String response = processUserMessage(userText);
            if (!response.isEmpty()) {
                sendResponse(chatId, response);
            }
        }
    }

    private String processUserMessage(String userText) {
        userText = userText.toLowerCase();

        if (userText.startsWith("hello, how are you?") || userText.startsWith("hello, how do you feel?")) {
            return "Hello, I am fine, and you?";
        } else if (userText.equals("i am fine") || userText.equals("i am well")) {
            return "It's nice to hear";
        } else if (userText.equals("show me support or contact of admin") || userText.equals("support")) {
            return "Write him @Bob1kGav for fixes";
        } else if (userText.equals("i think well")) {
            return "It's happy to hear it";
        } else if (userText.equals("hello, i need help, can you help me?") || userText.equals("hello, can you help me")) {
            return "How can I help you? If I can't help, then write ideas @Bob1kGav for fixes or new tasks)))";
        } else if (userText.equals("can you solve this example?")) {
            return "Of course, write the example below";
        } else if (userText.equals("find sin")) {
            return "Write the number to find the sine";
        } else if (userText.startsWith("result = ")) {
            String expression = userText.substring("result = ".length()).trim();
            try {
                double result = evaluateExpression(expression);
                return "Result: " + result;
            } catch (Exception e) {
                return "Error in evaluating the expression";
            }
        } else if (userText.startsWith("sqrt ")) {
            String numberStr = userText.substring("sqrt ".length()).trim();
            try {
                double number = Double.parseDouble(numberStr);
                double sqrtResult = Math.sqrt(number);
                return "Square root of " + number + " is " + sqrtResult;
            } catch (NumberFormatException e) {
                return "Invalid number format for square root";
            }
        } else if (userText.startsWith("sin ")) {
            String numberStr = userText.substring("sin ".length()).trim();
            try {
                double number = Double.parseDouble(numberStr);
                double sinResult = Math.sin(number);
                return "sin root of " + number + " is " + sinResult;
            } catch (NumberFormatException e) {
                return "Invalid number format for sin";
            }
        } else if (userText.startsWith("cos ")) {
            String numberStr = userText.substring("cos ".length()).trim();
            try {
                double number = Double.parseDouble(numberStr);
                double sinResult = Math.cos(number);
                return "cos root of " + number + " is " + sinResult;
            } catch (NumberFormatException e) {
                return "Invalid number format for cos";
            }
        } else if (userText.startsWith("log ")) {
            String numberStr = userText.substring("log ".length()).trim();
            try {
                double number = Double.parseDouble(numberStr);
                double sinResult = Math.log(number);
                return "log root of " + number + " is " + sinResult;
            } catch (NumberFormatException e) {
                return "Invalid number format for log";
            }
        } else {
            return "I don't understand. Please ask me to find sin, sqrt, cos, log, or solve a mathematical expression.";
        }
    }


    private double evaluateExpression(String expression) {
        try {
            Expression exp = new ExpressionBuilder(expression).build();
            return exp.evaluate();
        } catch (Exception e) {
            throw new RuntimeException("Error in evaluating the expression");
        }
    }

    private void sendResponse(long chatId, String text) {
        if (!text.isEmpty()) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(text);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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
