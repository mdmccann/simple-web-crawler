package com.github.mmccann94.simplewebcrawler.service;

import com.github.mmccann94.simplewebcrawler.domain.SiteMap;
import com.github.mmccann94.simplewebcrawler.exception.ResultOutputException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResultService {

  private static final String OUTPUT_FILE_NAME = "sitemap.html";
  private static final String TEMPLATE_NAME = "sitemap_template.ftl";
  private static final String BASE_URL_PLACEHOLDER = "BASE_URL";
  private static final String PAGE_LINK_MAP_PLACEHOLDER = "PAGE_LINK_MAP";
  private static final String BROKEN_LINKS_PLACEHOLDER = "BROKEN_LINKS";

  private final Configuration freemarkerConfig;

  void publishSiteMapResults(SiteMap siteMap) {
    try (Writer writer = new FileWriter(OUTPUT_FILE_NAME)) {
      Template template = freemarkerConfig.getTemplate(TEMPLATE_NAME);
      template.process(getTemplateDataModel(siteMap), writer);
    } catch (TemplateException | IOException e) {
      throw new ResultOutputException(String.format("Failed to output results for %s", siteMap.getBaseUrl()), e);
    }
  }

  private Map<String, Object> getTemplateDataModel(SiteMap siteMap) {
    Map<String, Object> templateDataModel = new HashMap<>();
    templateDataModel.put(BASE_URL_PLACEHOLDER, siteMap.getBaseUrl());
    templateDataModel.put(PAGE_LINK_MAP_PLACEHOLDER, siteMap.getPageLinksMap());
    templateDataModel.put(BROKEN_LINKS_PLACEHOLDER, siteMap.getBrokenLinks());
    return templateDataModel;
  }

}
