package com.github.mmccann94.simplewebcrawler.cli;

import com.github.mmccann94.simplewebcrawler.domain.CrawlInputProperties;
import com.github.mmccann94.simplewebcrawler.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class Runner implements CommandLineRunner {

  private final CrawlerService crawlerService;

  @Override
  public void run(String... args) {
    validateNumberOfArgs(args);

    CrawlInputProperties inputProperties = new CrawlInputProperties(args[0], Integer.parseInt(args[1]));

    log.info(String.format("Starting Simple Web Crawler for root url {%s} with {%d} threads.",
        inputProperties.getBaseUrl(), inputProperties.getNumOfThreads()));

    crawlerService.crawlUrlAndOutputToFile(inputProperties);
  }

  private void validateNumberOfArgs(String... args) {
    if (args.length < 2) {
      throw new IllegalArgumentException("A base url and number of threads must be supplied.");
    }
  }

}
