package weatherinformationsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

// User class for authentication
class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

// WeatherData class to store weather information
class WeatherData {
    private double temperature;
    private double humidity;
    private double windSpeed;
    private String weatherCondition;
    private LocalDateTime timestamp;

    public WeatherData(double temperature, double humidity, double windSpeed, String weatherCondition) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.weatherCondition = weatherCondition;
        this.timestamp = LocalDateTime.now();
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format(
                "Weather Data [Time: %s]\n" +
                        "Temperature: %.1f°C\n" +
                        "Humidity: %.1f%%\n" +
                        "Wind Speed: %.1f km/h\n" +
                        "Condition: %s",
                timestamp, temperature, humidity, windSpeed, weatherCondition
        );
    }
}

// WeatherStation class to manage weather data for a specific location
class WeatherStation {
    private String location;
    private Map<LocalDateTime, WeatherData> weatherHistory;

    public WeatherStation(String location) {
        this.location = location;
        this.weatherHistory = new HashMap<>();
    }

    public void recordWeatherData(WeatherData data) {
        weatherHistory.put(data.getTimestamp(), data);
    }

    public WeatherData getCurrentWeather() {
        if (weatherHistory.isEmpty()) {
            return null;
        }
        return weatherHistory.values().stream()
                .max((w1, w2) -> w1.getTimestamp().compareTo(w2.getTimestamp()))
                .get();
    }

    public String getLocation() {
        return location;
    }
}

// Main WeatherInformationSystem class
public class WeatherInformationSystem {
    private Map<String, WeatherStation> stations;
    private List<User> users;

    public WeatherInformationSystem() {
        this.stations = new HashMap<>();
        this.users = new ArrayList<>();
        this.users.add(new User("admin", "1234")); 
    }

    public void addWeatherStation(String location) {
        stations.putIfAbsent(location, new WeatherStation(location));
    }

    public void recordWeatherData(String location, WeatherData data) {
        WeatherStation station = stations.get(location);
        if (station != null) {
            station.recordWeatherData(data);
        } else {
            throw new IllegalArgumentException("Weather station not found for location: " + location);
        }
    }

    public WeatherData getCurrentWeather(String location) {
        WeatherStation station = stations.get(location);
        if (station != null) {
            return station.getCurrentWeather();
        }
        return null;
    }

    public boolean authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WeatherInformationSystem wis = new WeatherInformationSystem();

        System.out.println("Weather Information System");

        // User login
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (wis.authenticateUser(username, password)) {
            System.out.println("Login successful!");

            System.out.print("Enter the location of the weather station: ");
            String location = scanner.nextLine();
            wis.addWeatherStation(location);

            System.out.print("Enter temperature (°C): ");
            double temperature = scanner.nextDouble();

            System.out.print("Enter humidity (%): ");
            double humidity = scanner.nextDouble();

            System.out.print("Enter wind speed (km/h): ");
            double windSpeed = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter weather condition (e.g., Sunny, Rainy, etc.): ");
            String weatherCondition = scanner.nextLine();

            // Create a WeatherData object and record it in the system
            WeatherData data = new WeatherData(temperature, humidity, windSpeed, weatherCondition);
            wis.recordWeatherData(location, data);

            // Display current weather
            WeatherData currentWeather = wis.getCurrentWeather(location);
            if (currentWeather != null) {
                System.out.println("Current weather in " + location + ":");
                System.out.println(currentWeather);
            } else {
                System.out.println("No weather data found for " + location);
            }
        } else {
            System.out.println("Login failed.");
        }

        scanner.close();
    }
}