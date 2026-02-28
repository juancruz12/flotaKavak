package com.kavak.flota.config;

import org.springframework.context.annotation.Configuration;
import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {

    public TimeZoneConfig() {
        // Establecer zona horaria global de Argentina al iniciar la aplicación
        TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
        System.setProperty("user.timezone", "America/Argentina/Buenos_Aires");
    }
}


