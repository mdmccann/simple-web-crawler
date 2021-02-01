package com.github.mmccann94.simplewebcrawler.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "crawler")
@Getter
@Setter
public class CrawlConfig {
  private int pollingTimeoutMillis;
  private int httpRetryDelayMillis;
  private int httpRetryLimit;
}
