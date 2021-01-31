package com.github.mmccann94.simplewebcrawler.domain.validation;

import com.github.mmccann94.simplewebcrawler.domain.CrawlProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrawlPropertiesValidatorTest {

  @Mock
  private CrawlProperties crawlProperties;

  private CrawlPropertiesValidator cut;

  @BeforeEach
  void setUp() {
    cut = new CrawlPropertiesValidator();
  }

  @Test
  void validate() {
    when(crawlProperties.getBaseUrl()).thenReturn("https://example.com");
    when(crawlProperties.getNumOfThreads()).thenReturn(1);
    cut.validate(crawlProperties);
  }

  @Test
  void validate_nullBaseUrl() {
    assertThrows(IllegalArgumentException.class, () -> cut.validate(crawlProperties));
  }

  @Test
  void validate_emptyBaseUrl() {
    when(crawlProperties.getBaseUrl()).thenReturn("");
    assertThrows(IllegalArgumentException.class, () -> cut.validate(crawlProperties));
  }

  @Test
  void validate_numOfThreadsLessThanOne() {
    when(crawlProperties.getBaseUrl()).thenReturn("https://example.com");
    when(crawlProperties.getNumOfThreads()).thenReturn(0);
    assertThrows(IllegalArgumentException.class, () -> cut.validate(crawlProperties));
  }


}