package com.alexshin.weatherapp;


import com.alexshin.weatherapp.model.dto.WeatherApiResponseDTO;
import com.alexshin.weatherapp.model.entity.Location;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.service.WeatherService;
import com.alexshin.weatherapp.util.HibernateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

public class Main {

    public static void main1(String[] args) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();


        User user = User.builder()
                .login("username@mail.ru")
                .password("password")
                .build();

        Location location = Location.builder()
                .name("kek")
                .latitude(BigDecimal.valueOf(43.2213))
                .longitude(BigDecimal.valueOf(-34.65))
                .user(user)
                .build();

        Transaction transaction = null;
        try (var session = sessionFactory.openSession()) {
            session.getTransaction();
            transaction = session.beginTransaction();


//            session.persist(user);
//            session.persist(location);

//            User user1 = session.get(User.class, 2L);
//            session.remove(user1);

            transaction.commit();
            System.out.println();


        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }

    }


    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

        var objectMapper = new ObjectMapper();
        var service = WeatherService.getInstance();
        String response = "{\"coord\":{\"lon\":-0.1257,\"lat\":51.5085},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":292.31,\"feels_like\":291.95,\"temp_min\":291.24,\"temp_max\":293.43,\"pressure\":1016,\"humidity\":64,\"sea_level\":1016,\"grnd_level\":1012},\"visibility\":10000,\"wind\":{\"speed\":0.45,\"deg\":20,\"gust\":1.79},\"clouds\":{\"all\":100},\"dt\":1725461631,\"sys\":{\"type\":2,\"id\":2075535,\"country\":\"GB\",\"sunrise\":1725427113,\"sunset\":1725475245},\"timezone\":3600,\"id\":2643743,\"name\":\"London\",\"cod\":200}";
        String multipleWeathersResponse = "{\"coord\":{\"lon\":-0.1257,\"lat\":51.5085},\"weather\":[{\"id\":701,\"main\":\"Mist\",\"description\":\"mist\",\"icon\":\"50d\"},{\"id\":300,\"main\":\"Drizzle\",\"description\":\"light intensity drizzle\",\"icon\":\"09d\"}],\"base\":\"stations\",\"main\":{\"temp\":292.31,\"feels_like\":291.95,\"temp_min\":291.24,\"temp_max\":293.43,\"pressure\":1016,\"humidity\":64,\"sea_level\":1016,\"grnd_level\":1012},\"visibility\":10000,\"wind\":{\"speed\":0.45,\"deg\":20,\"gust\":1.79},\"clouds\":{\"all\":100},\"dt\":1725461631,\"sys\":{\"type\":2,\"id\":2075535,\"country\":\"GB\",\"sunrise\":1725427113,\"sunset\":1725475245},\"timezone\":3600,\"id\":2643743,\"name\":\"London\",\"cod\":200}";
//        String multipleWeathersResponse = "{\"message\":\"accurate\",\"cod\":\"200\",\"count\":1,\"list\":[{\"id\":2643743,\"name\":\"London\",\"coord\":{\"lat\":51.5085,\"lon\":-0.1258},\"main\":{\"temp\":280.15,\"pressure\":1012,\"humidity\":81,\"temp_min\":278.15,\"temp_max\":281.15},\"dt\":1485791400,\"wind\":{\"speed\":4.6,\"deg\":90},\"sys\":{\"country\":\"GB\"},\"rain\":null,\"snow\":null,\"clouds\":{\"all\":90},\"weather\":[{\"id\":701,\"main\":\"Mist\",\"description\":\"mist\",\"icon\":\"50d\"},{\"id\":300,\"main\":\"Drizzle\",\"description\":\"light intensity drizzle\",\"icon\":\"09d\"}]}]}";

        String name = "Madrid";
        WeatherApiResponseDTO resp1 = objectMapper.readValue(multipleWeathersResponse, WeatherApiResponseDTO.class);
        var resp = service.getWeatherByLocationName(name);
        var resp2 = service.getWeatherByLocationCoords(resp.getCoord().getLat(), resp.getCoord().getLon());
        var resp3 = service.searchLocationsByName(name);
        System.out.println(response);
        System.out.println(resp);





    }

}
