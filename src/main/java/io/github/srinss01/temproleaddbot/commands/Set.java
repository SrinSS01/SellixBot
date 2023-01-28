package io.github.srinss01.temproleaddbot.commands;

import io.github.srinss01.temproleaddbot.Config;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Set {
    final Config config;
    final String name = "set";
}
