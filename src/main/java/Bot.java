import service.TelegramBotConn;
import service.TelegramBotService;
import settings.InternetSettings;

public class Bot {
    private static final String TOKEN = System.getenv("TOKEN");
    private static final String BOT_USERNAME = System.getenv("BOT_USERNAME");
    private static final String PORT = System.getenv("PORT");
    private static final String SLEEP_TIME = System.getenv("SLEEP");

    public static void main(String[] args) {
        InternetSettings.proxyConfigure(false);

        TelegramBotConn telegramBotService = new TelegramBotService();
        boolean isConnected = telegramBotService.init(TOKEN, BOT_USERNAME);

        InternetSettings.startListener(Integer.parseInt(PORT), Integer.parseInt(SLEEP_TIME), isConnected);
    }
}
