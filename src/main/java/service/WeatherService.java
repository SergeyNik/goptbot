package service;

import model.Model;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WeatherService {
    private static Logger log = Logger.getLogger(WeatherService.class.getName());
    private static final String URL_WEATHER_OKTYABRSKY =
            "https://api.openweathermap.org/data/2.5/weather?lat=54.4871&lon=53.4294&units=metric&appid=4fa2c8b174a41896bd35edb7a29485ff";
    private OkHttpClient client = new OkHttpClient();

    private String getWeatherInfo() throws IOException {
        Request request = new Request.Builder()
                .url(URL_WEATHER_OKTYABRSKY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    public String getWeather() {
        Model model = new Model();
        try {
            String weatherInfo = getWeatherInfo();
            JSONObject json = new JSONObject(weatherInfo);
            JSONObject main = json.getJSONObject("main");
            model.setTemp(main.getDouble("temp"));
            log.log(Level.INFO, "message was send = " + json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Температура гоптябрьска: " + model.getTemp() + " ℃";
    }
}
