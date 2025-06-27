package com.lucas.plinks.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "plink")
@Getter
@Setter
public class PlinksProperties {
    private String domain;
}
