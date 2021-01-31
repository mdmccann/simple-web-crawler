package com.github.mmccann94.simplewebcrawler.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SiteMapTest {

  private static final String BASE_URL = "https://example.com";

  private SiteMap cut;

  @BeforeEach
  void setUp() {
    cut = new SiteMap(BASE_URL);
  }

  @Test
  void getBaseUrl() {
    assertEquals(BASE_URL, cut.getBaseUrl());
  }

  @Test
  void getPageLinksMap() {
    assertNotNull(cut.getPageLinksMap());
  }

  @Test
  void getBrokenLinks() {
    assertNotNull(cut.getBrokenLinks());
  }
}