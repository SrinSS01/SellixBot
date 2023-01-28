package io.github.srinss01.temproleaddbot;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableConfigurationProperties
@ConfigurationProperties("bot")
@Getter
@Setter
@ToString
public class Config {
    private String token;
    private List<String> temporaryRoleIds;
    private String sellixAuth;
    private List<String> admins;
    private String webhookChannels;
    private String roleToGive;
    private long timePeriodInSeconds;
}
