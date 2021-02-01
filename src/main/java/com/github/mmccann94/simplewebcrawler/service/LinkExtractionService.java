package com.github.mmccann94.simplewebcrawler.service;

import com.github.mmccann94.simplewebcrawler.service.port.LinkExtractionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkExtractionService {

  private final LinkExtractionPort linkExtractionPort;

  public Set<String> getContainingLinksOnPage(String url, String baseUrl) {
    return linkExtractionPort.getLinksLocatedOnPage(url)
        .stream()
        .filter(link -> isInternalLink(link, baseUrl))
        .filter(this::isNotFile)
        .map(this::cleanUrl)
        .collect(Collectors.toSet());
  }

  private boolean isInternalLink(String url, String baseUrl) {
    return url.startsWith(baseUrl);
  }

  private boolean isNotFile(String url) {
    return !url.substring(url.lastIndexOf("/") + 1).contains(".");
  }

  private String cleanUrl(String url) {
    String finalPartOfUrl = url.substring(url.lastIndexOf("/") + 1);

    if (finalPartOfUrl.contains("#")) {
      return cleanUrl(url.substring(0, url.lastIndexOf("#")));
    }
    if (finalPartOfUrl.equals("")) {
      return cleanUrl(url.substring(0, url.length() - 1));
    }

    return url;
  }

}
