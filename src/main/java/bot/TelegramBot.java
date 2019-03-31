package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.WeatherService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TelegramBot extends TelegramLongPollingBot {
    private static Logger log = Logger.getLogger(TelegramBot.class.getName());

    private final String TOKEN;
    private final String BOT_USERNAME;

    public TelegramBot(String token, String username) {
        this.TOKEN = token;
        this.BOT_USERNAME = username;
    }

    @Override
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

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
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
    private void setButtons(SendMessage sendMessage) {
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
}
