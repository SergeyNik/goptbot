package service;

import bot.TelegramBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TelegramBotService implements TelegramBotConn {
    private static Logger log = Logger.getLogger(TelegramBotService.class.getName());
    private TelegramBotsApi api = new TelegramBotsApi();

    @Override
    public boolean init(String token, String name) {
        ApiContextInitializer.init();
        try {
            log.log(Level.INFO, "Try to register!");
            api.registerBot(new TelegramBot(token, name));
            log.log(Level.INFO, "Registration completed successfully!");
            return true;
        } catch (TelegramApiRequestException e) {
            log.log(Level.SEVERE, "Register failed: ", e);
            return false;
        }
    }
}
