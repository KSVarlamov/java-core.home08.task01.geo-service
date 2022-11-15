package ru.netology.sender;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    public static final GeoServiceImpl GEO_SERVICE = Mockito.mock(GeoServiceImpl.class);
    public static final LocalizationService LOCALIZATION_SERVICE = Mockito.mock(LocalizationService.class);
    public static final MessageSender MESSAGE_SENDER = new MessageSenderImpl(GEO_SERVICE, LOCALIZATION_SERVICE);
    private static final Location RU_LOCATION = new Location("Moscow", Country.RUSSIA, "def street", 1);
    private static final Location USA_LOCATION = new Location("New York", Country.USA, "def street", 1);

    @BeforeAll
    public static void mockAll() {
        Mockito.when(GEO_SERVICE.byIp(MOSCOW_IP)).thenReturn(RU_LOCATION);
        Mockito.when(GEO_SERVICE.byIp("172.10.10.1")).thenReturn(RU_LOCATION);
        Mockito.when(GEO_SERVICE.byIp(NEW_YORK_IP)).thenReturn(USA_LOCATION);
        Mockito.when(GEO_SERVICE.byIp("92.20.10.11")).thenReturn(USA_LOCATION);
        Mockito.when(GEO_SERVICE.byIp(LOCALHOST)).thenReturn(USA_LOCATION);
        Mockito.when(LOCALIZATION_SERVICE.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");
        Mockito.when(LOCALIZATION_SERVICE.locale(Country.USA)).thenReturn("Welcome");
    }

    @ParameterizedTest
    @ValueSource(strings = {MOSCOW_IP, "172.10.10.1"})
    public void sendRightRusIP(String ip) {
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        assertEquals(MESSAGE_SENDER.send(headers), "Добро пожаловать");
    }

    @ParameterizedTest
    @ValueSource(strings = {NEW_YORK_IP, "92.20.10.11", LOCALHOST})
    public void sendNotRusIP(String ip) {
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        assertEquals(MESSAGE_SENDER.send(headers), "Welcome");
    }
}
