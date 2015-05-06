/**
 * This file is part of Alfresco/Apache Storm demo project.
 *
 *  Alfresco/Apache Storm demo project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  Alfresco/Apache Storm demo project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with  Alfresco/Apache Storm demo project.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.zaizi.alfresco.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.digitalpebble.storm.crawler.Constants;
import com.digitalpebble.storm.crawler.Metadata;
import com.digitalpebble.storm.crawler.persistence.Status;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ProcessNodes  extends BaseRichBolt {
    OutputCollector _collector;

    @Override
    public void prepare(Map conf, TopologyContext context,
                        OutputCollector collector) {
        _collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {

        Iterator<String> iterator = tuple.getFields().iterator();
        while (iterator.hasNext()) {
            String fieldName = iterator.next();
            Object obj = tuple.getValueByField(fieldName);

            if (obj instanceof byte[]) {
                System.out.println(fieldName + "\t"
                        + tuple.getBinaryByField(fieldName).length + " bytes");
                System.out.println(fieldName + "\t"
                        + tuple.getValueByField(fieldName) + " something");
                try {
                    String object =  ((byte[]) obj).toString();
                    System.out.println(object);
                } catch (Exception ex) {

                }
            }
            else if (obj instanceof Metadata) {
                Metadata md = (Metadata) obj;
                System.out.println(md.toString(fieldName + "."));
            } else {
                String value = tuple.getValueByField(fieldName).toString();
                System.out.println(fieldName + " : " + trimValue(value));
                try {
                    JSONObject object = (JSONObject) new JSONParser().parse(value);
                    Set keys = object.keySet();
                    Metadata metadata = new Metadata();
                    String url = "";
                    for(Object key : keys) {
                        System.out.println(key.toString() + " : " + object.get(key));
                        metadata.setValue(key.toString(), object.get(key).toString());

                        if (key.toString().equals("propertiesUrl")){
                            url = object.get(key).toString();
                        }
                    }

                    metadata.setValue("status","processed");

                    _collector.emit(
                            com.digitalpebble.storm.crawler.Constants.StatusStreamName,
                            tuple, new Values(url, metadata, Status.FETCHED));

                    System.out.println("status  : fetched");
                    System.out.println("bolt  : " + ProcessNodes.class.getName() );
                    System.out.println();

                } catch (Exception exc) {

                }
            }

        }





        _collector.ack(tuple);
    }

    private String trimValue(String value) {
        if (value.length() > 100)
            return value.length() + " chars";
        return value;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("url", "content", "metadata"));
        declarer.declareStream(Constants.StatusStreamName, new Fields("url",
                "metadata", "status"));
    }

}
