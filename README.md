alfresco-apache-storm-demo
=============
This demo project will fetch all the nodes from Alfresco to Apache Storm to process.(For demonstration purposes, nodes are being fetched and printed.) Then [Apache Storm](https://storm.apache.org/) continuously checks for changed nodes using Alfresco indexer webscript. If any change is detected, project will fetch them and process them.

To run this demo, you need a running Alfresco instance with [alfresco-indexer](https://github.com/maoo/alfresco-indexer) AMP. Then follow below instructions. (Instructions were extracted from storm-crawler project.)

### Running in local mode
To get started with alfresco-apache-storm-demo, it's recommended that you run the CrawlTopology in local mode.
 
NOTE: These instructions assume that you have Maven installed.

First, clone the project from github:
 
 ``` sh
 git clone https://github.com/zaizi/alfresco-apache-storm-demo.git
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


Alfresco/Apache Storm demo is developed using [storm-crawler](https://github.com/DigitalPebble/storm-crawler) from [@DigitalPebble](https://github.com/DigitalPebble) and [alfresco-indexer](https://github.com/maoo/alfresco-indexer). 
