package com.github.mmccann94.simplewebcrawler.service.port;

import java.util.Set;

public interface LinkExtractionPort {

  Set<String> getLinksLocatedOnPage(String url);

}
