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

package com.digitalpebble.storm.crawler.persistence;

public enum Status {
    DISCOVERED, FETCHED, FETCH_ERROR, REDIRECTION, ERROR;

    /** Maps the HTTP Code to FETCHED, FETCH_ERROR or REDIRECTION */
    public static Status fromHTTPCode(int code) {
        if (code == 200)
            return Status.FETCHED;
        else if (code == 304)
            return Status.FETCHED;
        // REDIRS?
        if (code >= 300 && code < 400)
            return Status.REDIRECTION;
        // error otherwise
        return Status.FETCH_ERROR;
    }

}
