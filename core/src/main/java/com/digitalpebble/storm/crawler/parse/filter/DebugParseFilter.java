/**
 * Licensed to DigitalPebble Ltd under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * DigitalPebble licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.digitalpebble.storm.crawler.parse.filter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DocumentFragment;

import com.digitalpebble.storm.crawler.Metadata;
import com.digitalpebble.storm.crawler.parse.Outlink;
import com.digitalpebble.storm.crawler.parse.ParseFilter;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Dumps the DOM representation of a document into a file
 */
public class DebugParseFilter implements ParseFilter {

    private OutputStream os;

    @Override
    public void filter(String URL, byte[] content, DocumentFragment doc,
            Metadata metadata, List<Outlink> outlinks) {

        try {
            XMLSerializer serializer = new XMLSerializer(os, null);
            serializer.serialize(doc);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure(Map stormConf, JsonNode filterParams) {
        try {
            File outFile = File.createTempFile("DOMDump", ".txt");
            os = FileUtils.openOutputStream(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean needsDOM() {
        return true;
    }
}
