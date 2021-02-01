package com.github.mmccann94.simplewebcrawler.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrawlInputProperties {
  String baseUrl;
  int numOfThreads;
}
