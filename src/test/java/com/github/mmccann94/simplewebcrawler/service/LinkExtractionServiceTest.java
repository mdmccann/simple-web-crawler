package com.github.mmccann94.simplewebcrawler.service;

import com.github.mmccann94.simplewebcrawler.service.port.LinkExtractionPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkExtractionServiceTest {

  private static final String URL = "https://example.com";

  @Mock
  private LinkExtractionPort linkExtractionPort;

  private LinkExtractionService sut;

  @BeforeEach
  void setUp() {
    sut = new LinkExtractionService(linkExtractionPort);
  }

  @Test
  void getContainingLinks_filtersOutExternalLinks() {
    when(linkExtractionPort.getLinksLocatedOnPage(URL)).thenReturn(Collections.singleton("http://facebook.com"));
    assertEquals(0, sut.getContainingLinks(URL, URL).size());
  }

  @Test
  void getContainingLinks_filtersOutLinksToOtherSubdomains() {
    when(linkExtractionPort.getLinksLocatedOnPage(URL)).thenReturn(Collections.singleton("http://sub.example.com/page/"));
    assertEquals(0, sut.getContainingLinks(URL, URL).size());
  }

  @Test
  void getContainingLinks_filtersOutLinksToFiles() {
    when(linkExtractionPort.getLinksLocatedOnPage(URL)).thenReturn(Collections.singleton("https://example.com/contact-us/how-to-contact.pdf"));
    assertEquals(0, sut.getContainingLinks(URL, URL).size());
  }

  @Test
  void getContainingLinks_removesAnchors() {
    when(linkExtractionPort.getLinksLocatedOnPage(URL)).thenReturn(Collections.singleton("https://example.com/another-page#some-heading"));
    assertTrue(sut.getContainingLinks(URL, URL).contains("https://example.com/another-page"));
  }

  @Test
  void getContainingLinks_removesEndingSlash() {
    when(linkExtractionPort.getLinksLocatedOnPage(URL)).thenReturn(Collections.singleton("https://example.com/another-page/"));
    assertTrue(sut.getContainingLinks(URL, URL).contains("https://example.com/another-page"));
  }

}