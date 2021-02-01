package com.github.mmccann94.simplewebcrawler.service.port.adapter;

import com.github.mmccann94.simplewebcrawler.domain.CrawlConfig;
import com.github.mmccann94.simplewebcrawler.exception.UnreachableLinkException;
import com.github.mmccann94.simplewebcrawler.service.port.LinkExtractionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class JsoupLinkExtractionAdapter implements LinkExtractionPort {

  private final CrawlConfig crawlConfig;

  @Override
  public Set<String> getLinksLocatedOnPage(String url) {
    return getUrlDocument(url).select("a[href]")
        .stream()
        .map(element -> element.attr("abs:href"))
        .collect(Collectors.toSet());

  }

  private Document getUrlDocument(String url) {
    int attempts = 0;
    Document document = null;
    while (isNull(document))
      try {
        document = Jsoup.connect(url).get();
      } catch (IOException e) {
        if (attempts >= crawlConfig.getHttpRetryLimit()) {
          log.error(String.format("Failed to load url [%s].", url));
          throw new UnreachableLinkException(String.format("Could not extract links from %s", url), e);
        }
        attempts++;
        try {
          Thread.sleep(crawlConfig.getHttpRetryDelayMillis());
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
        }
        log.error(String.format("Failed to load url [%s], retrying. Attempt: [%d/%d]", url, attempts, crawlConfig.getHttpRetryLimit()));
      }

    return document;
  }


}
