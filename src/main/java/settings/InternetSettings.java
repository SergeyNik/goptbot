package settings;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InternetSettings {
    private static Logger log = Logger.getLogger(InternetSettings.class.getName());

    public static void proxyConfigure(boolean flag) {
        if (flag) {
            System.getProperties().put("proxySet", "true");
            System.getProperties().put("socksProxyHost", "127.0.0.1");
            System.getProperties().put("socksProxyPort", "9050");
        }
    }
    public static void startListener(int port, int sleep, boolean start) {
        if (start) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    Thread.sleep(sleep);
                }
            } catch (IOException | InterruptedException e) {
                log.log(Level.SEVERE, "Server error ", e);
            }
        }
    }
}
