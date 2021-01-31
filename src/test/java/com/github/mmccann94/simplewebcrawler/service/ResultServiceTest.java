package com.github.mmccann94.simplewebcrawler.service;

import com.github.mmccann94.simplewebcrawler.domain.SiteMap;
import freemarker.template.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultServiceTest {

  private static final String BASE_URL = "https://example.com";
  private static Set<String> BROKEN_LINKS = Set.of("https://example.com/broken");

  @Mock
  private SiteMap siteMap;

  private ResultService sut;

  @BeforeEach
  void setUp() throws Exception {
    when(siteMap.getBaseUrl()).thenReturn(BASE_URL);
    when(siteMap.getPageLinksMap()).thenReturn(getPageLinksMap());
    when(siteMap.getBrokenLinks()).thenReturn(BROKEN_LINKS);

    Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
    configuration.setDirectoryForTemplateLoading(ResourceUtils.getFile("classpath:templates"));
    sut = new ResultService(configuration);
  }

  @Test
  void publishSiteMapResults() throws Exception {
    sut.publishSiteMapResults(siteMap);
    String expectedHtml = Files.readString(ResourceUtils.getFile("classpath:expected-sitemap.html").toPath());
    String actualHtml = Files.readString(Path.of("sitemap.html"));
    assertEquals(expectedHtml, actualHtml);
  }

  private ConcurrentHashMap<String, Set<String>> getPageLinksMap() {
    ConcurrentHashMap<String, Set<String>> pageLinksMap = new ConcurrentHashMap<>();
    pageLinksMap.put("https://example.com", Set.of("https://example.com/1"));
    pageLinksMap.put("https://example.com/1", Set.of("https://example.com"));
    return pageLinksMap;
  }

}