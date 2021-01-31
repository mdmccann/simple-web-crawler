package com.github.mmccann94.simplewebcrawler.domain;

import com.github.mmccann94.simplewebcrawler.domain.validation.CrawlPropertiesValidator;
import com.github.mmccann94.simplewebcrawler.service.LinkExtractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteCrawlerTest {

  @Mock
  private CrawlPropertiesValidator propertiesValidator;
  @Mock
  private LinkExtractionService linkExtractionService;
  @Mock
  private CrawlProperties crawlProperties;

  private SiteCrawler cut;

  @BeforeEach
  void setUp() {
    when(crawlProperties.getBaseUrl()).thenReturn("https://example.com");
    when(crawlProperties.getNumOfThreads()).thenReturn(4);
    cut = new SiteCrawler(propertiesValidator, linkExtractionService);
  }

  @Test
  void crawl_callsValidator() {
    cut.crawl(crawlProperties);
    verify(propertiesValidator).validate(crawlProperties);
  }

}