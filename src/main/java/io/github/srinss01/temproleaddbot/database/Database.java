package io.github.srinss01.temproleaddbot.database;

import io.github.srinss01.temproleaddbot.Config;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Getter
public class Database {
    Config config;
    UserOrdersRepository userOrdersRepository;
}
