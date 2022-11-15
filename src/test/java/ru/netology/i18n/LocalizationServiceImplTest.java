package ru.netology.i18n;

import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalizationServiceImplTest {
    private final static LocalizationService localizationService = new LocalizationServiceImpl();

    @Test
    void localeRU() {
        assertEquals(localizationService.locale(Country.RUSSIA), "Добро пожаловать");
    }

    @Test
    public void localeUSA() {
        assertEquals(localizationService.locale(Country.USA), "Welcome");
    }

    @Test
    public void localeBrazil() {
        assertEquals(localizationService.locale(Country.BRAZIL), "Welcome");
    }
}