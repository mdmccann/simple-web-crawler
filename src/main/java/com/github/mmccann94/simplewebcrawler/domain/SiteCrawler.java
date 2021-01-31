package com.github.mmccann94.simplewebcrawler.domain;

import com.github.mmccann94.simplewebcrawler.domain.validation.CrawlPropertiesValidator;
import com.github.mmccann94.simplewebcrawler.service.LinkExtractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class SiteCrawler {

  private final CrawlPropertiesValidator propertiesValidator;
  private final LinkExtractionService linkExtractionService;

  public SiteMap crawl(CrawlProperties properties) {
    propertiesValidator.validate(properties);

    SiteMap siteMap = new SiteMap(properties.getBaseUrl());
    LinkedBlockingQueue<String> newLinks = new LinkedBlockingQueue<>();
    newLinks.add(properties.getBaseUrl());

    ExecutorService executorService = Executors.newFixedThreadPool(properties.getNumOfThreads());
    List<Callable<Void>> pageCrawlers = new ArrayList<>(properties.getNumOfThreads());
    for (int i = 0; i < properties.getNumOfThreads(); i++) {
      pageCrawlers.add(new PageCrawler(linkExtractionService, newLinks, siteMap));
    }
    try {
      executorService.invokeAll(pageCrawlers);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      log.debug("Executor Service shutting down.");
      executorService.shutdown();
    }

    return siteMap;
  }

}
