package com.surfsoftconsulting.clipper.racetracker.web;

/*
 * Copyright 2019 Phil Haigh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RaceNumberParser {

    private final Logger LOGGER = LoggerFactory.getLogger(RaceNumberParser.class);

    private static final String RACENO_CLASS = "raceno";
    private static final String RACE_NUMBER_PREFIX = "Race ";

    public int parse(Document standingsPage) {

        Elements elements = standingsPage.body().getElementsByClass(RACENO_CLASS);
        if (elements.size() != 1) {
            String message = String.format("Found %s instances of class %s when expecting exactly one", elements.size(), RACENO_CLASS);
            LOGGER.error(message);
            throw new IllegalStateException(message);
        }

        String raceNoText = elements.get(0).text().trim();
        if (!raceNoText.startsWith(RACE_NUMBER_PREFIX)) {
            String message = String.format("Race number text '%s' doesn't start with the expected prefix '%s' - can't parse", raceNoText, RACE_NUMBER_PREFIX);
            LOGGER.error(message);
            throw new IllegalStateException(message);
        }

        String raceNumberAsString = raceNoText.substring(RACE_NUMBER_PREFIX.length());
        try {
            return Integer.parseInt(raceNumberAsString);
        }
        catch(NumberFormatException e) {
            String message = String.format("Race number '%s' isn't a valid number", raceNumberAsString);
            LOGGER.error(message);
            throw new IllegalStateException(message, e);

        }

    }

}
