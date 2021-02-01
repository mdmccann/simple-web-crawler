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
  private final CrawlConfig crawlConfig;

  /**
   * Creates a number of PageCrawlers equal to the amount of processing threads inputted
   * and passes shared memory objects such as siteMap and linksToProcess. After all PageCrawler's
   * have finished execution, the SiteMap is returned.
   */
  public SiteMap crawl(CrawlInputProperties inputProperties) {
    propertiesValidator.validate(inputProperties);

    SiteMap siteMap = new SiteMap(inputProperties.getBaseUrl());
    LinkedBlockingQueue<String> linksToProcess = new LinkedBlockingQueue<>();
    linksToProcess.add(inputProperties.getBaseUrl());

    ExecutorService executorService = Executors.newFixedThreadPool(inputProperties.getNumOfThreads());
    List<Callable<Void>> pageCrawlers = new ArrayList<>(inputProperties.getNumOfThreads());
    for (int i = 0; i < inputProperties.getNumOfThreads(); i++) {
      pageCrawlers.add(new PageCrawler(linkExtractionService, crawlConfig, linksToProcess, siteMap));
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
