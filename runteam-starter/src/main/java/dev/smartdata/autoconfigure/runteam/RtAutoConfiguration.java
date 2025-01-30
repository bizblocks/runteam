package dev.smartdata.autoconfigure.runteam;

import dev.smartdata.runteam.RtConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({RtConfiguration.class})
public class RtAutoConfiguration {
}

