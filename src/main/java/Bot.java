import service.TelegramBotService;
import settings.TorProxySettings;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Bot {
    private static Logger log = Logger.getLogger(Bot.class.getName());

    private static final String TOKEN = System.getenv("TOKEN");
    private static final String BOT_USERNAME = System.getenv("BOT_USERNAME");
    private static final String PORT = System.getenv("PORT");
    private static final String SLEEP_TIME = System.getenv("SLEEP");

    public static void main(String[] args) {
        TorProxySettings.configure(false);

        TelegramBotService telegramBotService = new TelegramBotService();
        boolean isConnected = telegramBotService.init(TOKEN, BOT_USERNAME);

        if (isConnected) {
            try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(PORT))) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    Thread.sleep(Integer.parseInt(SLEEP_TIME));
                }
            } catch (IOException | InterruptedException e) {
                log.log(Level.SEVERE, "Server error ", e);
            }
        }
    }
}
