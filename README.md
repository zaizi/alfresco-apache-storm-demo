alfresco-apache-storm-demo
=============
First this will fetch all the nodes from Alfresco repository and process them. (For demonstration purposes, nodes are being fetched and printed.) Then [Apache Storm](https://storm.apache.org/) continously checks for changed nodes using Alfresco indexer webscript. If any change is detected, project will fetch them and process them.

Alfresco/Apache Storm demo is developed using [storm-crawler](https://github.com/DigitalPebble/storm-crawler) from [@DigitalPebble](https://github.com/DigitalPebble). 

To run this demo, you need a running Alfresco instance with [alfresco-indexer](https://github.com/maoo/alfresco-indexer) AMP.

### Running in local mode
To get started with storm-crawler, it's recommended that you run the CrawlTopology in local mode.
 
NOTE: These instructions assume that you have Maven installed.

First, clone the project from github:
 
 ``` sh
 git clone https://github.com/DigitalPebble/storm-crawler
 ```
 
Then :
``` sh
cd core
mvn clean compile exec:java -Dstorm.topology=com.digitalpebble.storm.crawler.CrawlTopology -Dexec.args="-conf crawler-conf.yaml -local"
```
to run the demo CrawlTopology.

### On a Storm cluster
Alternatively, generate an uberjar:
``` sh
mvn clean package
```

and then submit the topology with `storm jar`:

``` sh
storm jar target/storm-crawler-core-0.5-SNAPSHOT-jar-with-dependencies.jar  com.digitalpebble.storm.crawler.CrawlTopology -conf crawler-conf.yaml
```

to run it in distributed mode.
