package com.github.mmccann94.simplewebcrawler.service;

import com.github.mmccann94.simplewebcrawler.domain.CrawlConfig;
import com.github.mmccann94.simplewebcrawler.domain.CrawlInputProperties;
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
  private final CrawlConfig crawlConfig;
  private final ResultService resultService;

  public void crawlUrlAndOutputToFile(CrawlInputProperties crawlInputProperties) {
    SiteCrawler siteCrawler = new SiteCrawler(crawlPropertiesValidator, linkExtractionService, crawlConfig);
    SiteMap siteMap = siteCrawler.crawl(crawlInputProperties);

    log.info("Crawling complete. Publishing results...");
    resultService.publishSiteMapResults(siteMap);
  }

}
