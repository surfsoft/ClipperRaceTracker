package com.surfsoftconsulting.clipper.racetracker.web;

/*
 * Copyright 2017 Phil Haigh
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Component
public class SpeedAndCourseDataParser {

    private final Logger LOGGER = LoggerFactory.getLogger(SpeedAndCourseDataParser.class);

    private static final String PREFIX = "content: \"";
    private static final String SUFFIX = "\"";

    private final SpeedAndCourseDataFactory speedAndCourseDataFactory;

    public SpeedAndCourseDataParser(SpeedAndCourseDataFactory speedAndCourseDataFactory) {
        this.speedAndCourseDataFactory = speedAndCourseDataFactory;
    }

    public List<SpeedAndCourseData> parse(Document raceStandingsPage) {

        // Find the correct script tag
        String script = null;
        Elements scripts = raceStandingsPage.body().getElementsByTag("script");
        for (int index = 0; index < scripts.size() && script == null; index++) {
            if (scripts.get(index).dataNodes().size() > 0 && scripts.get(index).dataNodes().get(0).getWholeData().contains("<span>SOG</span>")) {
                script = scripts.get(index).dataNodes().get(0).getWholeData();
            }
        }

        // Find the lines containing the HTML
        if (script != null) {
            return Arrays.stream(script.split("\n"))
                    .filter(s -> s.contains("<span>SOG</span>"))
                    .map(s -> {
                        String javaScript = s.trim();
                        String html = format("<html><body><div id=\"infoPanel\">%s</div></body></html>", javaScript.substring(PREFIX.length(), javaScript.length() - SUFFIX.length()).replace("\\", ""));
                        Document document = Jsoup.parse(html);

                        Element infoPanel = document.body().getElementById("infoPanel");
                        return new SpeedAndCourseData(speedAndCourseDataFactory.getName(infoPanel), speedAndCourseDataFactory.getSpeed(infoPanel), speedAndCourseDataFactory.getHeading(infoPanel));
                    })
                    .collect(toList());
        }
        else {
            LOGGER.warn("No speed and course data found");
            return emptyList();
        }

    }

}
