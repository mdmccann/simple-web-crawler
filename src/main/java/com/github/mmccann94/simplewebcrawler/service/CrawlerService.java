package com.github.mmccann94.simplewebcrawler.service;

import com.github.mmccann94.simplewebcrawler.domain.CrawlProperties;
import com.github.mmccann94.simplewebcrawler.domain.SiteCrawler;
import com.github.mmccann94.simplewebcrawler.domain.SiteMap;
import com.github.mmccann94.simplewebcrawler.domain.validation.CrawlPropertiesValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlerService {

  private final CrawlPropertiesValidator crawlPropertiesValidator;
  private final LinkExtractionService linkExtractionService;
  private final ResultService resultService;

  public void crawlUrlAndOutputToFile(CrawlProperties crawlProperties) {
    SiteCrawler siteCrawler = new SiteCrawler(crawlPropertiesValidator, linkExtractionService);
    SiteMap siteMap = siteCrawler.crawl(crawlProperties);

    log.info("Crawling complete. Publishing results...");
    resultService.publishSiteMapResults(siteMap);
  }

}
