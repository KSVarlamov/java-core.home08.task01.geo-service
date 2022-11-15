package ru.netology.sender;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageServiceTest {
    public static final String LOCALHOST = "127.0.0.1";
    public static final String MOSCOW_IP = "172.0.32.11";
    public static final String NEW_YORK_IP = "96.44.183.149";
    private final Location RU_LOCATION = new Location("Moscow", Country.RUSSIA, "def street", 1);
    private final Location USA_LOCATION = new Location("New York", Country.USA, "def street", 1);

    @Test
    public void testSMessageSenderImpl() {
        GeoServiceImpl geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(MOSCOW_IP)).thenReturn(RU_LOCATION);
        Mockito.when(geoService.byIp(NEW_YORK_IP)).thenReturn(USA_LOCATION);
        Mockito.when(geoService.byIp(LOCALHOST)).thenReturn(USA_LOCATION);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");
        Mockito.when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, MOSCOW_IP);
        assertEquals(messageSender.send(headers), "Добро пожаловать");

        headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, NEW_YORK_IP);
        assertEquals(messageSender.send(headers), "Welcome");

        headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, LOCALHOST);
        assertEquals(messageSender.send(headers), "Welcome");
    }

}
