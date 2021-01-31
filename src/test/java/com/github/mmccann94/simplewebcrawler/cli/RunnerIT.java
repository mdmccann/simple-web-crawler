package com.github.mmccann94.simplewebcrawler.cli;

import com.github.mmccann94.simplewebcrawler.service.CrawlerService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class RunnerIT {

  @Autowired
  private CrawlerService crawlerService;

  private WireMockServer wireMockServer = new WireMockServer();

  @BeforeEach
  void setUp() throws Exception {
    removeLocalSiteMapFileIfExists();
    wireMockServer.start();
  }

  @AfterEach()
  void shutdown() {
    wireMockServer.stop();
  }

  @Test
  void run() {
    givenIExpectCallsToWebsite();
    whenTheCrawlerIsRan();
    thenTheResultsHtmlIsOutputted();
  }

  private void givenIExpectCallsToWebsite() {
    wireMockServer.stubFor(get(WireMock.urlEqualTo("/"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("content-type", "text/html")
            .withBody("<html><body><a href=\"/contacts\">Contacts</a></body></html>")));

    wireMockServer.stubFor(get(WireMock.urlEqualTo("/contacts"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("content-type", "text/html")
            .withBody("<html><body><a href=\"/\">Home</a></body></html>")));
  }

  private void whenTheCrawlerIsRan() {
    new Runner(crawlerService).run("http://localhost:8080", "1");
  }

  private void thenTheResultsHtmlIsOutputted() {
    assertTrue(Files.exists(Path.of("sitemap.html")));
  }

  private void removeLocalSiteMapFileIfExists() throws IOException {
    if (Files.exists(Path.of("sitemap.html"))) {
      Files.delete(Path.of("sitemap.html"));
    }
  }

}