package com.github.mmccann94.simplewebcrawler.domain.validation;

import com.github.mmccann94.simplewebcrawler.domain.CrawlProperties;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class CrawlPropertiesValidator {

  public void validate(CrawlProperties crawlProperties) {

    if (isNull(crawlProperties.getBaseUrl()) || crawlProperties.getBaseUrl().strip().equals("")) {
      throw new IllegalArgumentException("Invalid base url supplied: " + crawlProperties.getBaseUrl());
    }

    if (crawlProperties.getNumOfThreads() < 1) {
      throw new IllegalArgumentException("Number of threads must be greater than zero.");
    }
  }

}
