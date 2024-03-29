package com.github.mmccann94.simplewebcrawler.cli;

import com.github.mmccann94.simplewebcrawler.domain.CrawlInputProperties;
import com.github.mmccann94.simplewebcrawler.service.CrawlerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RunnerTest {

  @Mock
  private CrawlerService crawlerService;

  private Runner runner;

  @BeforeEach
  void setUp() {
    runner = new Runner(crawlerService);
  }

  @Test
  void run() {
    runner.run("https://example.com", "4");
    verify(crawlerService, times(1)).crawlUrlAndOutputToFile(new CrawlInputProperties("https://example.com", 4));
  }

  @Test
  void run_notEnoughArgs() {
    assertThrows(IllegalArgumentException.class, () -> runner.run("https://example.com"));
  }

}