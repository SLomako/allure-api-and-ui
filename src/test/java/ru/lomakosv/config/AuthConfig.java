package ru.lomakosv.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:config/test.properties"
})
public interface AuthConfig extends Config {

    String username();

    String password();

    @Key("token")
    String token();

    @Key("xsrfToken")
    String xsrfToken();

    String projectId();
}
