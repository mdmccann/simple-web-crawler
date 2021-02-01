package com.github.mmccann94.simplewebcrawler.domain.validation;

import com.github.mmccann94.simplewebcrawler.domain.CrawlInputProperties;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class CrawlPropertiesValidator {

  public void validate(CrawlInputProperties crawlInputProperties) {

    if (isNull(crawlInputProperties.getBaseUrl()) || crawlInputProperties.getBaseUrl().strip().equals("")) {
      throw new IllegalArgumentException("Invalid base url supplied: " + crawlInputProperties.getBaseUrl());
    }

    if (crawlInputProperties.getNumOfThreads() < 1) {
      throw new IllegalArgumentException("Number of threads must be greater than zero.");
    }
  }

}
