import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//@Log
public class Bot extends TelegramLongPollingBot {
    private static Logger log = Logger.getLogger(Bot.class.getName());
    private static final String TOKEN = System.getenv("TOKEN");
    private static final String BOT_USERNAME = System.getenv("BOT_USERNAME");
    private static final String PORT = System.getenv("PORT");

    // receive message
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message == null || !message.hasText()) {
            return;
        }

        String codeword = message.getText();

        // new user in group
        List<User> newChatMembers = message.getNewChatMembers();
        if (newChatMembers != null && !newChatMembers.isEmpty()) {
            for (User user : newChatMembers) {
                sendMessage(message, "Дарова епта " + user.getUserName() + " эээээээээ" );
            }
        }

        // commands
        if ("/help".equals(codeword)) {
            sendMessage(message, "What can I do for you?");
        } else if ("/settings".equals(codeword)) {
            sendMessage(message, "What will configure?");
        } else if ("погода".equalsIgnoreCase(codeword)) {
            sendMessage(message, new WeatherService().getWeather());
        }
    }

    private void sendMessage(Message message, String text) {
        SendMessage msg = new SendMessage();
        msg.enableMarkdown(true);
        msg.setChatId(message.getChatId().toString());
        msg.setReplyToMessageId(message.getMessageId());
        msg.setText(text);
        try {
            setButtons(msg);
            execute(msg);
            log.log(Level.INFO, "message was send = " + msg);
        } catch (TelegramApiException e) {
            log.log(Level.SEVERE, "Failed sending:  ", e);
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(keyboardMarkup);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
//        firstRow.add(new KeyboardButton("/help"));
//        firstRow.add(new KeyboardButton("/settings"));
        firstRow.add(new KeyboardButton("Погода"));
        keyboardRowList.add(firstRow);
        keyboardMarkup.setKeyboard(keyboardRowList);
    }

    public String getBotUsername() {
        return BOT_USERNAME;
    }

    public String getBotToken() {
        return TOKEN;
    }

    public static void main(String[] args) {
        // TOR
//        System.getProperties().put("proxySet", "true");
//        System.getProperties().put("socksProxyHost", "127.0.0.1");
//        System.getProperties().put("socksProxyPort", "9050");
        // ------------------------------------------------------


        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            log.log(Level.INFO, "Try to register!");
            api.registerBot(new Bot());
            log.log(Level.INFO, "Register successful!");
        } catch (TelegramApiRequestException e) {
            log.log(Level.SEVERE, "Register failed: ", e);
        }

        try (ServerSocket serverSocket = new ServerSocket(Integer.valueOf(PORT))) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
