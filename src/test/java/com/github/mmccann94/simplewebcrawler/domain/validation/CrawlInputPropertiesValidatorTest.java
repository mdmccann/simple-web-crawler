package com.github.mmccann94.simplewebcrawler.domain.validation;

import com.github.mmccann94.simplewebcrawler.domain.CrawlInputProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrawlInputPropertiesValidatorTest {

  @Mock
  private CrawlInputProperties crawlInputProperties;

  private CrawlPropertiesValidator cut;

  @BeforeEach
  void setUp() {
    cut = new CrawlPropertiesValidator();
  }

  @Test
  void validate() {
    when(crawlInputProperties.getBaseUrl()).thenReturn("https://example.com");
    when(crawlInputProperties.getNumOfThreads()).thenReturn(1);
    cut.validate(crawlInputProperties);
  }

  @Test
  void validate_nullBaseUrl() {
    assertThrows(IllegalArgumentException.class, () -> cut.validate(crawlInputProperties));
  }

  @Test
  void validate_emptyBaseUrl() {
    when(crawlInputProperties.getBaseUrl()).thenReturn("");
    assertThrows(IllegalArgumentException.class, () -> cut.validate(crawlInputProperties));
  }

  @Test
  void validate_numOfThreadsLessThanOne() {
    when(crawlInputProperties.getBaseUrl()).thenReturn("https://example.com");
    when(crawlInputProperties.getNumOfThreads()).thenReturn(0);
    assertThrows(IllegalArgumentException.class, () -> cut.validate(crawlInputProperties));
  }


}