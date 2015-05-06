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
package com.zaizi.alfresco.spouts;

import java.util.*;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import com.github.maoo.indexer.client.AlfrescoClient;
import com.github.maoo.indexer.client.AlfrescoFilters;
import com.github.maoo.indexer.client.AlfrescoResponse;
import com.github.maoo.indexer.client.WebScriptsAlfrescoClient;
import com.zaizi.alfresco.AlfrescoConstants;
import org.json.simple.JSONObject;

/**
 * Fetch changes nodes from Alfresco using Alfresco indexer.
 */
@SuppressWarnings("serial")
public class AlfrescoSpout extends BaseRichSpout {
    SpoutOutputCollector _collector;
    Random _rand;
    AlfrescoClient client;
    List<Map<String, Object>> list;
    Iterator<Map<String, Object>> listIterator;
    long lastTransactionId = 0;
    long lastChangesetId = 0;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        _rand = new Random();
        client = new WebScriptsAlfrescoClient(AlfrescoConstants.PROTOCOL, AlfrescoConstants.HOSTNAME, AlfrescoConstants.ENDPOINT,
                AlfrescoConstants.STORE_PROTOCOL, AlfrescoConstants.STORE_ID, AlfrescoConstants.USERNAME, AlfrescoConstants.PASSWORD);
    }

    public void fetchNodes() {
        AlfrescoResponse response = client.fetchNodes(lastTransactionId, lastChangesetId, new AlfrescoFilters());
        list = response.getDocumentList();
        lastTransactionId = response.getLastTransactionId();
        lastChangesetId = response.getLastAclChangesetId();
        listIterator = list.iterator();
    }

    @Override
    public void nextTuple() {
        if(listIterator!= null && listIterator.hasNext()) {
            // Returning node by node
            Map<String, Object> item = listIterator.next();
            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, Object> mapEntry : item.entrySet()) {
                jsonObject.put(mapEntry.getKey(), mapEntry.getValue());
            }

            _collector.emit(new Values(jsonObject.toJSONString()));

            listIterator.remove();
        } else {
            // Fetch changed nodes if list is empty.
            Utils.sleep(1000);
            fetchNodes();
        }
    }

    @Override
    public void ack(Object id) {
    }

    @Override
    public void fail(Object id) {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("node"));
    }

}
