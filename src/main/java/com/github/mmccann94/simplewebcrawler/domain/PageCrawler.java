package com.github.mmccann94.simplewebcrawler.domain;

import com.github.mmccann94.simplewebcrawler.exception.UnreachableLinkException;
import com.github.mmccann94.simplewebcrawler.service.LinkExtractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Slf4j
public class PageCrawler implements Callable<Void> {

  private static final int POLLING_TIMEOUT_MILLIS = 5000;

  private final LinkExtractionService linkExtractionService;
  private final LinkedBlockingQueue<String> pagesToLookup;
  private final SiteMap siteMap;

  @Override
  public Void call() throws InterruptedException {
    while (true) {
      String newPageUrl = pagesToLookup.poll(POLLING_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
      log.debug(String.format("Thread ID: {%d} has picked up URL: %s", Thread.currentThread().getId(), newPageUrl));

      if (isNull(newPageUrl)) {
        log.debug(String.format("Queue is empty, Thread ID {%d} exiting.", Thread.currentThread().getId()));
        break;
      }

      try {
        Set<String> containingLinks = linkExtractionService.getContainingLinks(newPageUrl, siteMap.getBaseUrl());
        siteMap.getPageLinksMap().put(newPageUrl, containingLinks);
        addNewlyDiscoveredLinksToQueue(containingLinks);

      } catch (UnreachableLinkException e) {
        log.error("Broken link found: " + newPageUrl);
        siteMap.getBrokenLinks().add(newPageUrl);
      }
    }
    return null;
  }

  private void addNewlyDiscoveredLinksToQueue(Set<String> links) throws InterruptedException {
    for (String link : links) {
      if (isNull(siteMap.getPageLinksMap().putIfAbsent(link, new HashSet<>()))) {
        pagesToLookup.put(link);
      }
    }
  }

}
