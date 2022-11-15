import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

public class TestMessageService {
    private final Location RU_LOCATION = new Location("Moscow", Country.RUSSIA, "def street", 1);
    private final Location USA_LOCATION = new Location("New York", Country.USA, "def street", 1);

    public static final String LOCALHOST = "127.0.0.1";
    public static final String MOSCOW_IP = "172.0.32.11";
    public static final String NEW_YORK_IP = "96.44.183.149";

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

    @Test
    public void TestGeoServiceImpl() {
        GeoService geoService = new GeoServiceImpl();
        Location msk = geoService.byIp(MOSCOW_IP);
        assertEquals(msk.getCountry(), Country.RUSSIA);
        assertEquals(msk.getCity(), "Moscow");

        msk = geoService.byIp("172.13.13.13");
        assertEquals(msk.getCountry(), Country.RUSSIA);
        assertEquals(msk.getCity(), "Moscow");

        Location usa = geoService.byIp(NEW_YORK_IP);
        assertEquals(usa.getCountry(), Country.USA);
        assertEquals(usa.getCity(), "New York");

        geoService.byIp("94.200.10.10");
        assertEquals(usa.getCountry(), Country.USA);
        assertEquals(usa.getCity(), "New York");
    }

    @Test
    public void TestLocalizationServiceImpl() {
        LocalizationService localizationService = new LocalizationServiceImpl();
        assertEquals(localizationService.locale(Country.RUSSIA), "Добро пожаловать");
        assertEquals(localizationService.locale(Country.USA), "Welcome");
        assertEquals(localizationService.locale(Country.BRAZIL), "Welcome");
    }
}
