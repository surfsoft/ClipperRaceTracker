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

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class SpeedAndCourseDataFactory {

    public String getName(Element infoPanel) {
        return infoPanel.getElementsByTag("h3").get(0).text().trim();
    }

    public Double getSpeed(Element infoPanel) {
        String sog = getDivValue(infoPanel,"SOG").substring(0, getDivValue(infoPanel,"SOG").length() - 2);
        return StringUtils.isBlank(sog) ? null : Double.valueOf(sog);

    }

    public Integer getHeading(Element infoPanel) {
        String cog = getDivValue(infoPanel,"COG");
        return StringUtils.isBlank(cog) ? null : Integer.valueOf(cog);

    }

    private String getDivValue(Element infoPanel, String label) {

        String value = null;

        Elements divs = infoPanel.getElementsByTag("div");
        for (int index = 0; index < divs.size() && value == null; index++) {
            if (!divs.get(index).hasClass("markerInfo")) {
                Elements cells = divs.get(index).getElementsByTag("span");
                if (cells.get(0).text().trim().equalsIgnoreCase(label) && cells.size() >= 2) {
                    value = cells.get(1).text().trim();
                }
            }
        }

        return value;

    }
}
