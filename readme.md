# Simple Web Crawler

Given a domain name e.g. https://example.com, Simple Web Crawler will crawl the website and list all pages located on 
that subdomain and the containing links of each page in table HTML format.

### Prerequisites 
* Java
* Maven

### Getting Started
Run the following commands from this directory:
1) `mvn clean compile assembly:single`
2) `cd target `

####Running the JAR
`java -jar simple-web-crawler-0.0.1-SNAPSHOT {baseUrl} {numOfThreads}` 
#####Arguments
* {baseUrl} - The URL that should be crawled (e.g. https://example.com)
* {numOfThreads} - The number of threads that will be used for execution

####Viewing Results
After the program has finished executing, view the results by exploring the HTML files inside the <i>results</i> directory.

## Built With
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)
* [Project Lombok](https://projectlombok.org/)
* [Freemarker](https://freemarker.apache.org/)
* [Wiremock](http://wiremock.org/)

##Improvements
* Execution is currently limited to memory available as the crawl results are stored in memory before being published to file. 
  Period memory flushing to disk could be utilised to free up RAM storage.
  
