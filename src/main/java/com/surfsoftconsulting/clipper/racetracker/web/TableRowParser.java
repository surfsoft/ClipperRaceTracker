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

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TableRowParser {

    private static final DateTimeFormatter TIMESTAMP_PARSER = DateTimeFormatter.ofPattern("dd MMMM yyyy H.m");

    public Integer getPosition(Element tableRow) {
        String positionString = tableRow.getElementsByTag("td").get(0).getElementsByClass("num").text().trim();
        if (positionString.equals("-")) {
            return 99; // Stealth mode, this is a hack for now
        }
        else {
            return Integer.valueOf(positionString);
        }
    }

    public String getName(Element tableRow) {
        return tableRow.getElementsByTag("td").get(1).getElementsByTag("a").text().trim();
    }

    public Double getLatitude(Element tableRow) {
        if (isInStealthMode(tableRow)) {
            return null;
        }
        else {
            return Double.parseDouble(tableRow.getElementsByTag("td").get(2).text().trim());
        }
    }

    public Double getLongitude(Element tableRow) {
        if (isInStealthMode(tableRow)) {
            return null;
        }
        else {
            return Double.parseDouble(tableRow.getElementsByTag("td").get(3).text().trim());
        }
    }

    public Double getDistanceRemaining(Element tableRow) {
        if (isInStealthMode(tableRow)) {
            return null;
        }
        else {
            return parseNauticalMiles(tableRow.getElementsByTag("td").get(4).text().trim());
        }
    }

    public Double getDistanceToLeadVessel(Element tableRow) {
        if (isInStealthMode(tableRow)) {
            return null;
        }
        else {
            if (getPosition(tableRow) == 1 || isInStealthMode(tableRow)) {
                return null;
            } else {
                return parseNauticalMiles(tableRow.getElementsByTag("td").get(5).text().trim());
            }
        }
    }

    public Double getDistanceTravelled(Element tableRow) {
        if (isInStealthMode(tableRow)) {
            return null;
        }
        else {
            return parseNauticalMiles(tableRow.getElementsByTag("td").get(6).text().trim());
        }
    }

    public LocalDateTime getTimestamp(Element tableRow) {
        String timestampString = tableRow.getElementsByTag("td").get(7).text().trim();
        if (isInStealthMode(tableRow)) {
            String untilString = timestampString.substring(timestampString.toLowerCase().indexOf("until:") + 7);
            return LocalDateTime.parse(untilString, TIMESTAMP_PARSER);
        }
        else {
            if (StringUtils.isBlank(timestampString)) {
                return null;
            } else {
                return LocalDateTime.parse(timestampString.substring(0, timestampString.length() - 6), TIMESTAMP_PARSER);
            }
        }
    }

    public String getStatus(Element tableRow) {
        return tableRow.getElementsByTag("td").get(8).text().trim();
    }

    public LocalDateTime getFinishTime(Element tableRow) {
        String finishTimeString = tableRow.getElementsByTag("td").get(9).text().trim();
        if (StringUtils.isBlank(finishTimeString)) {
            return null;
        }
        else if (finishTimeString.equalsIgnoreCase("finished")) {
            finishTimeString = tableRow.getElementsByTag("td").get(10).text().trim();
            return LocalDateTime.parse(finishTimeString.substring(0, finishTimeString.length() - 6), TIMESTAMP_PARSER);
        }
        else {
            return LocalDateTime.parse(finishTimeString.substring(0, finishTimeString.length() - 6), TIMESTAMP_PARSER);
        }
    }

    public boolean isInStealthMode(Element tableRow) {
        return tableRow.hasClass("stealthrow");
    }

    private Double parseNauticalMiles(String nauticalMilesString) {
        final Double nauticalMiles;
        if (nauticalMilesString.length() == 0) {
            nauticalMiles = (double) 0;
        }
        else {
            nauticalMiles = Double.valueOf(nauticalMilesString.substring(0, nauticalMilesString.length() - 2));
        }
        return nauticalMiles;
    }
}
