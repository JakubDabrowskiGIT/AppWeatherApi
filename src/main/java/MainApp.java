import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainApp implements Runnable {

    private Scanner scanner;

    private void startApp() {
        scanner = new Scanner(System.in);
        System.out.println("0 Zakończ działanie \nWpisz dla ilu dni chcesz wyświetlić pogodę z przedziału od 1 do 5");
        Integer days = scanner.nextInt();

        if(days == 0) {
            return;
        }

        while (days < 1 || days > 6) {
            System.out.println("Liczba dni musi być z przedziału 1 do 5: ");
            days = scanner.nextInt();
        }

        System.out.println("Wybierz po czym chcesz znaleźć miejsce dla którego wyświetlisz pogodę \n1 - Nazwa Miasta \n2 - Kod pocztowy");
        Integer action = scanner.nextInt();

        chooseTypeSearching(action, days);
    }

    private void chooseTypeSearching(int typeNumber, int days) {
        String urlPath;
        switch (typeNumber) {
            case 1:
                urlPath = getUrlPathForCityName();
                getWeatherDetails(urlPath, days);
                startApp();
                break;
            case 2:
                urlPath = getUrlPathForZipCode();
                getWeatherDetails(urlPath, days);
                startApp();
                break;
        }
    }

    private void getWeatherDetails(String urlPath, int days) {
        String response;
        if (days == 1) {
            response = getWeatherForCurrent(urlPath);
            System.out.println(parseJson(response));
        } else {
            response = getWeatherForXDays(urlPath, days);
            List<Weather> weatherList = parseJsonFor5Days(response);
            printWeatherForXDays(weatherList, days);
        }
    }

    private String getUrlPathForCityName() {
        System.out.println("Podaj nazwę miasta: ");
        String cityName = scanner.next();
        String url = "?q=" + cityName;
        return url;
    }

    private String getUrlPathForZipCode() {
        System.out.println("Podaj kod pocztowy miasta: ");
        String zipcode = scanner.next();
        String url = "?zip=" + zipcode;
        return url;
    }

    public String getWeatherForCurrent(String url) {
        String response = null;
        try {
            response = new HttpService().connect(Config.APP_URL + url + "&lang=pl&appid=" + Config.APP_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getWeatherForXDays(String url, int days) {
        String response = null;
        try {
            response = new HttpService().connect(Config.APP_URL_FOR_5DAYS + url + ",pl&lang=pl&appid=" + Config.APP_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private Weather parseJson(String json) {
        Weather weather = new Weather();
        JSONObject rootObject = new JSONObject(json);
        if (rootObject.getInt("cod") == 200) {
            double temp;
            JSONObject mainObject = rootObject.getJSONObject("main");
            DecimalFormat df = new DecimalFormat("#.##");
            temp = mainObject.getDouble("temp");
            temp = temp - 273;

            JSONObject cloudsObject = rootObject.getJSONObject("clouds");

            weather.setTemp(df.format(temp));
            weather.setHumidity(mainObject.getDouble("humidity"));
            weather.setClouds(cloudsObject.getInt("all"));
            weather.setPressure(mainObject.getInt("pressure"));

        } else {
            System.out.println("Error");
        }
        return weather;
    }

    private List<Weather> parseJsonFor5Days(String json) {
        JSONObject rootObject = new JSONObject(json);
        List<Weather> weatherListFor5Days = new ArrayList<>();
        if (rootObject.getInt("cod") == 200) {
            JSONArray jsonArrayMain = rootObject.getJSONArray("list");
            for (int i = 0; i < jsonArrayMain.length(); i++) {
                JSONObject weatherObject = (JSONObject) jsonArrayMain.get(i);
                if (weatherObject.getString("dt_txt").endsWith("12:00:00")) {
                    JSONObject jsonObjectMainWeather = weatherObject.getJSONObject("main");
                    DecimalFormat df = new DecimalFormat("#.#");
                    double tempDouble = jsonObjectMainWeather.getDouble("temp");
                    tempDouble = tempDouble - 273;

                    JSONArray weatherDetailsArray = weatherObject.getJSONArray("weather");
                    JSONObject weatherDetailsObject = (JSONObject) weatherDetailsArray.get(0);

                    Weather weather = new Weather();
                    weather.setTemp(df.format(tempDouble));
                    weather.setHumidity(jsonObjectMainWeather.getDouble("humidity"));
                    weather.setPressure(jsonObjectMainWeather.getInt("pressure"));
                    weather.setClouds(weatherObject.getJSONObject("clouds").getInt("all"));
                    weather.setWind(weatherObject.getJSONObject("wind").getDouble("speed"));
                    weather.setDescription(weatherDetailsObject.getString("description"));
                    weather.setDate(weatherObject.getString("dt_txt"));

                    weatherListFor5Days.add(weather);
                }

            }
        } else {
            System.out.println("Error");
        }

        return weatherListFor5Days;
    }

    private void printWeatherForXDays(List<Weather> weatherList, int days) {
        if (weatherList.size() < days) {
            System.out.println("Zbyt mało danych!");
            return;
        }
        System.out.println("Pogoda dla wybranego miasta na " + days + " dni:");
        for (int i = 0; i < days; i++) {
            Weather weather = weatherList.get(i);
            System.out.println(weather.getDate());
            System.out.print("Temperatura: " + weather.getTemp() + " \u00b0C");
            System.out.print(" Wilgotność: " + weather.getHumidity() + " %");
            System.out.print(" Zachmurzenie: " + weather.getClouds() + "%");
            System.out.print(" Ciśnienie: " + weather.getPressure() + " hPa");
            System.out.println(" Opis pogody: " + weather.getDescription());
        }
    }

    @Override
    public void run() {
        startApp();
    }
}
