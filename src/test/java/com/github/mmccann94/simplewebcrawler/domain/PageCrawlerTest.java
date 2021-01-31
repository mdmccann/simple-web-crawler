package com.github.mmccann94.simplewebcrawler.domain;

import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;
import com.github.mmccann94.simplewebcrawler.exception.UnreachableLinkException;
import com.github.mmccann94.simplewebcrawler.service.LinkExtractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Timeout(value = 15)
class PageCrawlerTest {

  private static final String BASE_URL = "https://example.com";
  private static final String LINK_FROM_HOMEPAGE = "https://example.com/contact-us";

  @Mock
  private LinkExtractionService linkExtractionService;
  @Mock
  private SiteMap siteMap;
  private LinkedBlockingQueue<String> newLinks;

  private PageCrawler cut;

  @BeforeEach
  void setUp() {
    newLinks = new LinkedBlockingQueue<>();
    cut = new PageCrawler(linkExtractionService, newLinks, siteMap);
  }

  @Test
  void call_timesOutAfterTenSecondsOfNoWork() throws Exception {
    assertNull(cut.call());
  }

  @Test
  void call_populatesLinksOnPage() throws Exception {
    newLinks.put(BASE_URL);
    when(siteMap.getBaseUrl()).thenReturn(BASE_URL);
    when(linkExtractionService.getContainingLinks(BASE_URL, BASE_URL)).thenReturn(Set.of(LINK_FROM_HOMEPAGE));
    when(siteMap.getPageLinksMap()).thenReturn(new ConcurrentHashMap<>());
    cut.call();
    assertTrue(siteMap.getPageLinksMap().get(BASE_URL).contains(LINK_FROM_HOMEPAGE));
  }

  @Test
  void call_doesNotRequestLinksForAPageWhichIsAlreadyComplete() throws Exception {
    newLinks.put(BASE_URL);
    when(siteMap.getBaseUrl()).thenReturn(BASE_URL);
    when(linkExtractionService.getContainingLinks(BASE_URL, BASE_URL)).thenReturn(Set.of(BASE_URL));
    when(siteMap.getPageLinksMap()).thenReturn(new ConcurrentHashMap<>());
    cut.call();
    verify(linkExtractionService, times(1)).getContainingLinks(BASE_URL, BASE_URL);
  }

  @Test
  void call_addsToBrokenLinksCollection() throws Exception {
    newLinks.put(BASE_URL);
    when(siteMap.getBaseUrl()).thenReturn(BASE_URL);
    when(linkExtractionService.getContainingLinks(BASE_URL, BASE_URL)).thenThrow(new UnreachableLinkException("error"));
    when(siteMap.getBrokenLinks()).thenReturn(ConcurrentHashMap.newKeySet());
    cut.call();
    assertTrue(siteMap.getBrokenLinks().contains(BASE_URL));
  }

}