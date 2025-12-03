package com.scoring.github.popularity_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

@Setter
@Getter
@ConfigurationProperties(prefix = "webclient.buffer")
public class WebClientBufferProperties {

    private DataSize size = DataSize.ofMegabytes(10);

}

