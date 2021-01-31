package com.github.mmccann94.simplewebcrawler.domain;

import lombok.Getter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class SiteMap {

  private final String baseUrl;
  private ConcurrentHashMap<String, Set<String>> pageLinksMap;
  private Set<String> brokenLinks;

  public SiteMap(String baseUrl) {
    this.baseUrl = baseUrl;
    this.pageLinksMap = new ConcurrentHashMap<>();
    this.brokenLinks = ConcurrentHashMap.newKeySet();
  }

}
