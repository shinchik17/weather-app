package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.exception.service.weatherapi.WeatherApiCallException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

// TODO: these tests seem useless, consider replacing them by servlets tests
@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private HttpClient client;

    @Mock
    private HttpResponse<String> response;

    @InjectMocks
    private WeatherService weatherService;

    private final String locName = "London";

    private final String successWeatherResponseBody = "{\"coord\":{\"lon\":-0.1257,\"lat\":51.5085},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":15.49,\"feels_like\":15.34,\"temp_min\":14.36,\"temp_max\":16.4,\"pressure\":1008,\"humidity\":86,\"sea_level\":1008,\"grnd_level\":1004},\"visibility\":10000,\"wind\":{\"speed\":6.69,\"deg\":240},\"clouds\":{\"all\":100},\"dt\":1725962608,\"sys\":{\"type\":2,\"id\":2075535,\"country\":\"GB\",\"sunrise\":1725946083,\"sunset\":1725992823},\"timezone\":3600,\"id\":2643743,\"name\":\"London\",\"cod\":200}";


    @Test
    void searchLocationsByName_success() throws IOException, InterruptedException {
        String successGeocodingResponseBody = "[{\"name\":\"London\",\"local_names\":{\"sl\":\"London\",\"uz\":\"London\",\"bo\":\"ལོན་ཊོན།\",\"tk\":\"London\",\"hu\":\"London\",\"no\":\"London\",\"zu\":\"ILondon\",\"ug\":\"لوندۇن\",\"tt\":\"Лондон\",\"ce\":\"Лондон\",\"kl\":\"London\",\"oc\":\"Londres\",\"ie\":\"London\",\"gl\":\"Londres\",\"cv\":\"Лондон\",\"fj\":\"Lodoni\",\"ro\":\"Londra\",\"kn\":\"ಲಂಡನ್\",\"fr\":\"Londres\",\"mk\":\"Лондон\",\"ga\":\"Londain\",\"hi\":\"लंदन\",\"ia\":\"London\",\"sw\":\"London\",\"vi\":\"Luân Đôn\",\"az\":\"London\",\"be\":\"Лондан\",\"na\":\"London\",\"is\":\"London\",\"nl\":\"Londen\",\"bm\":\"London\",\"zh\":\"伦敦\",\"cs\":\"Londýn\",\"fy\":\"Londen\",\"id\":\"London\",\"yi\":\"לאנדאן\",\"sh\":\"London\",\"bi\":\"London\",\"my\":\"လန်ဒန်မြို့\",\"mr\":\"लंडन\",\"sd\":\"لنڊن\",\"et\":\"London\",\"jv\":\"London\",\"bg\":\"Лондон\",\"en\":\"London\",\"sv\":\"London\",\"qu\":\"London\",\"ms\":\"London\",\"wo\":\"Londar\",\"li\":\"Londe\",\"av\":\"Лондон\",\"mg\":\"Lôndôna\",\"pl\":\"Londyn\",\"cy\":\"Llundain\",\"sq\":\"Londra\",\"da\":\"London\",\"os\":\"Лондон\",\"fa\":\"لندن\",\"kw\":\"Loundres\",\"es\":\"Londres\",\"ig\":\"London\",\"mt\":\"Londra\",\"tl\":\"Londres\",\"fi\":\"Lontoo\",\"mi\":\"Rānana\",\"so\":\"London\",\"bh\":\"लंदन\",\"nn\":\"London\",\"uk\":\"Лондон\",\"sa\":\"लन्डन्\",\"th\":\"ลอนดอน\",\"ht\":\"Lonn\",\"eo\":\"Londono\",\"su\":\"London\",\"ff\":\"London\",\"bs\":\"London\",\"br\":\"Londrez\",\"sk\":\"Londýn\",\"ay\":\"London\",\"om\":\"Landan\",\"st\":\"London\",\"vo\":\"London\",\"ba\":\"Лондон\",\"sc\":\"Londra\",\"to\":\"Lonitoni\",\"ps\":\"لندن\",\"bn\":\"লন্ডন\",\"gn\":\"Lóndyre\",\"km\":\"ឡុងដ៍\",\"ml\":\"ലണ്ടൻ\",\"kv\":\"Лондон\",\"af\":\"Londen\",\"hy\":\"Լոնդոն\",\"io\":\"London\",\"co\":\"Londra\",\"nv\":\"Tooh Dineʼé Bikin Haalʼá\",\"se\":\"London\",\"lv\":\"Londona\",\"ky\":\"Лондон\",\"ny\":\"London\",\"wa\":\"Londe\",\"ur\":\"علاقہ لندن\",\"ku\":\"London\",\"gv\":\"Lunnin\",\"am\":\"ለንደን\",\"or\":\"ଲଣ୍ଡନ\",\"ee\":\"London\",\"ja\":\"ロンドン\",\"de\":\"London\",\"it\":\"Londra\",\"tw\":\"London\",\"pt\":\"Londres\",\"lo\":\"ລອນດອນ\",\"ar\":\"لندن\",\"rm\":\"Londra\",\"cu\":\"Лондонъ\",\"mn\":\"Лондон\",\"te\":\"లండన్\",\"sn\":\"London\",\"lb\":\"London\",\"tr\":\"Londra\",\"he\":\"לונדון\",\"lt\":\"Londonas\",\"ab\":\"Лондон\",\"ta\":\"இலண்டன்\",\"pa\":\"ਲੰਡਨ\",\"ne\":\"लन्डन\",\"sm\":\"Lonetona\",\"ko\":\"런던\",\"yo\":\"Lọndọnu\",\"tg\":\"Лондон\",\"kk\":\"Лондон\",\"ka\":\"ლონდონი\",\"fo\":\"London\",\"an\":\"Londres\",\"gd\":\"Lunnainn\",\"si\":\"ලන්ඩන්\",\"feature_name\":\"London\",\"ascii\":\"London\",\"ca\":\"Londres\",\"el\":\"Λονδίνο\",\"sr\":\"Лондон\",\"ha\":\"Landan\",\"ru\":\"Лондон\",\"gu\":\"લંડન\",\"ln\":\"Lóndɛlɛ\",\"hr\":\"London\",\"eu\":\"Londres\"},\"lat\":51.5073219,\"lon\":-0.1276474,\"country\":\"GB\",\"state\":\"England\"},{\"name\":\"City of London\",\"local_names\":{\"zh\":\"倫敦市\",\"pt\":\"Cidade de Londres\",\"es\":\"City de Londres\",\"he\":\"הסיטי של לונדון\",\"ko\":\"시티 오브 런던\",\"ru\":\"Сити\",\"ur\":\"لندن شہر\",\"lt\":\"Londono Sitis\",\"uk\":\"Лондонське Сіті\",\"en\":\"City of London\",\"fr\":\"Cité de Londres\",\"hi\":\"सिटी ऑफ़ लंदन\"},\"lat\":51.5156177,\"lon\":-0.0919983,\"country\":\"GB\",\"state\":\"England\"},{\"name\":\"London\",\"local_names\":{\"th\":\"ลอนดอน\",\"ga\":\"Londain\",\"ja\":\"ロンドン\",\"lv\":\"Landona\",\"fa\":\"لندن\",\"bn\":\"লন্ডন\",\"be\":\"Лондан\",\"he\":\"לונדון\",\"ru\":\"Лондон\",\"fr\":\"London\",\"iu\":\"ᓚᓐᑕᓐ\",\"el\":\"Λόντον\",\"ar\":\"لندن\",\"lt\":\"Londonas\",\"yi\":\"לאנדאן\",\"en\":\"London\",\"ko\":\"런던\",\"hy\":\"Լոնտոն\",\"ka\":\"ლონდონი\",\"oj\":\"Baketigweyaang\",\"ug\":\"لوندۇن\",\"cr\":\"ᓬᐊᐣᑕᐣ\"},\"lat\":42.9832406,\"lon\":-81.243372,\"country\":\"CA\",\"state\":\"Ontario\"},{\"name\":\"Chelsea\",\"local_names\":{\"it\":\"Chelsea\",\"de\":\"Chelsea\",\"ru\":\"Челси\",\"uk\":\"Челсі\",\"ko\":\"첼시\",\"hi\":\"चेल्सी, लंदन\",\"es\":\"Chelsea\",\"id\":\"Chelsea, London\",\"sk\":\"Chelsea\",\"fa\":\"چلسی\",\"et\":\"Chelsea\",\"vi\":\"Chelsea, Luân Đôn\",\"pl\":\"Chelsea\",\"sh\":\"Chelsea, London\",\"az\":\"Çelsi\",\"no\":\"Chelsea\",\"af\":\"Chelsea, Londen\",\"tr\":\"Chelsea, Londra\",\"sv\":\"Chelsea, London\",\"hu\":\"Chelsea\",\"da\":\"Chelsea\",\"ur\":\"چیلسی، لندن\",\"el\":\"Τσέλσι\",\"ga\":\"Chelsea\",\"nl\":\"Chelsea\",\"pt\":\"Chelsea\",\"eu\":\"Chelsea\",\"ar\":\"تشيلسي\",\"ja\":\"チェルシー\",\"fr\":\"Chelsea\",\"zh\":\"車路士\",\"en\":\"Chelsea\",\"he\":\"צ'לסי\"},\"lat\":51.4875167,\"lon\":-0.1687007,\"country\":\"GB\",\"state\":\"England\"},{\"name\":\"London\",\"lat\":37.1289771,\"lon\":-84.0832646,\"country\":\"US\",\"state\":\"Kentucky\"}]";

        Mockito.when(client.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
        Mockito.when(response.statusCode()).thenReturn(200);
        Mockito.when(response.body()).thenReturn(successGeocodingResponseBody);

        Assertions.assertFalse(weatherService.searchLocationsByName(locName).isEmpty());

    }

    @Test
    void searchLocationsByName_noLocationsFound() throws IOException, InterruptedException {
        String emptyGeocodRespBody = "[]";

        Mockito.when(client.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
        Mockito.when(response.statusCode()).thenReturn(200);
        Mockito.when(response.body()).thenReturn(emptyGeocodRespBody);

        Assertions.assertTrue(weatherService.searchLocationsByName(locName).isEmpty());
    }

    @Test
    void searchLocationsByName_statusCodeIsNotOk_thenThrows() throws IOException, InterruptedException {
        Mockito.when(client.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
        Mockito.when(response.statusCode()).thenReturn(400);

        Assertions.assertThrows(WeatherApiCallException.class, () -> weatherService.searchLocationsByName(locName));
    }

    @Test
    void getWeatherByLocationName_success() throws IOException, InterruptedException {
        Mockito.when(client.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
        Mockito.when(response.statusCode()).thenReturn(200);
        Mockito.when(response.body()).thenReturn(successWeatherResponseBody);

        Assertions.assertEquals(locName, weatherService.getWeatherByLocationName(locName).getCityName());
    }

    @Test
    void getWeatherByLocationName_statusCodeIsNotOk_thenThrows() throws IOException, InterruptedException {
        Mockito.when(client.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
        Mockito.when(response.statusCode()).thenReturn(400);

        Assertions.assertThrows(WeatherApiCallException.class, () -> weatherService.getWeatherByLocationName(locName));
    }


    @Test
    void getWeatherByLocationCoords_success() throws IOException, InterruptedException {
        BigDecimal lon = BigDecimal.valueOf(-0.1257);
        BigDecimal lat = BigDecimal.valueOf(51.5085);
        Mockito.when(client.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
        Mockito.when(response.statusCode()).thenReturn(200);
        Mockito.when(response.body()).thenReturn(successWeatherResponseBody);

        Assertions.assertEquals(locName, weatherService.getWeatherByLocationCoords(lat, lon).getCityName());
    }

    @Test
    void getWeatherByLocationCoords_statusCodeIsNotOk_thenThrows() throws IOException, InterruptedException {
        Mockito.when(client.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
        Mockito.when(response.statusCode()).thenReturn(400);

        Assertions.assertThrows(WeatherApiCallException.class, () -> weatherService.getWeatherByLocationName(locName));
    }


}
