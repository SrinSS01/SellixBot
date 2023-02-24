package io.github.srinss01.sellixbot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableConfigurationProperties
@ConfigurationProperties("bot")
@Getter
@Setter
public class Config {
    private String token;
    private List<String> temporaryRoleIds;
    private String key;
    private String logChannelId;
    private String sellixAuth;
    private String roleToGive;
    private long timePeriodInSeconds;

    static boolean isActivated = false;

    @Override
    public String toString() {
        return "bot:" + '\n' +
                "  " + "token: " + token + '\n' +
                "  " + "temporaryRoleIds: \n" + ListToYaml(temporaryRoleIds) +
                "  " + "key: " + key + '\n' +
                "  " + "logChannelId: " + logChannelId + '\n' +
                "  " + "sellixAuth: " + sellixAuth + '\n' +
                "  " + "roleToGive: " + roleToGive + '\n' +
                "  " + "timePeriodInSeconds: " + timePeriodInSeconds;
    }

    private static String ListToYaml(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append("    - ").append(s).append('\n');
        }
        return sb.toString();
    }
}
