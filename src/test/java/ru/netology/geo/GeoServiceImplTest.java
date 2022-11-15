package ru.netology.geo;

import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeoServiceImplTest {
    public static final String MOSCOW_IP = "172.0.32.11";
    public static final String NEW_YORK_IP = "96.44.183.149";

    private static GeoService geoService = new GeoServiceImpl();

    @Test
    public void TestMoscowIP() {
        Location msk = geoService.byIp(MOSCOW_IP);
        assertEquals(msk.getCountry(), Country.RUSSIA);
        assertEquals(msk.getCity(), "Moscow");
    }

    @Test
    public void TestNewYorkIP() {
        Location usa = geoService.byIp(NEW_YORK_IP);
        assertEquals(usa.getCountry(), Country.USA);
        assertEquals(usa.getCity(), "New York");
    }

    @Test
    public void TestSomeUSAIP() {
        Location usa =  geoService.byIp("96.22.200.10");
        assertEquals(usa.getCountry(), Country.USA);
        assertEquals(usa.getCity(), "New York");
    }

}
