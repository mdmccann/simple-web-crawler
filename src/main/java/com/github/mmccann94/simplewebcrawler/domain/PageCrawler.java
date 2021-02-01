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

/**
 * Polls the queue of outstanding links and assigns the links contained on that page.
 * In the case that any of these links have not been already discovered, the link is added
 * to the queue to be processed itself.
 * <p>
 * If the Crawler has been polling the processing queue in excess of the defined timeout, it will exit.
 */
@RequiredArgsConstructor
@Slf4j
public class PageCrawler implements Callable<Void> {

  private static final int POLLING_TIMEOUT_MILLIS = 5000;

  private final LinkExtractionService linkExtractionService;
  private final LinkedBlockingQueue<String> linksToProcess;
  private final SiteMap siteMap;

  @Override
  public Void call() throws InterruptedException {
    while (true) {
      String newPageUrl = linksToProcess.poll(POLLING_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
      log.debug(String.format("Thread ID: {%d} has picked up URL: %s", Thread.currentThread().getId(), newPageUrl));

      if (isNull(newPageUrl)) {
        log.debug(String.format("Queue is empty, Thread ID {%d} exiting.", Thread.currentThread().getId()));
        break;
      }

      try {
        Set<String> containingLinks = linkExtractionService.getContainingLinksOnPage(newPageUrl, siteMap.getBaseUrl());
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
        log.debug(String.format("Adding newly discovered link to queue %s", link));

        linksToProcess.put(link);
      }
    }
  }

}
