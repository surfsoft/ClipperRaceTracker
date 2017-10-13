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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RaceStandingsData {

    private static final DateTimeFormatter TIMESTAMP_PARSER = DateTimeFormatter.ofPattern("dd MMMM yyyy H.m");

    private final Element tableRow;

    RaceStandingsData(Element tableRow) {
        this.tableRow = tableRow;
    }

    public Integer getPosition() {
        return Integer.valueOf(tableRow.getElementsByTag("td").get(0).getElementsByClass("num").text().trim());
    }

    public String getName() {
        return tableRow.getElementsByTag("td").get(1).getElementsByTag("a").text().trim();
    }

    public Double getLatitude() {
        if (isInStealthMode()) {
            return null;
        }
        else {
            return Double.parseDouble(tableRow.getElementsByTag("td").get(2).text().trim());
        }
    }

    public Double getLongitude() {
        if (isInStealthMode()) {
            return null;
        }
        else {
            return Double.parseDouble(tableRow.getElementsByTag("td").get(3).text().trim());
        }
    }

    public Double getDistanceRemaining() {
        if (isInStealthMode()) {
            return null;
        }
        else {
            return parseNauticalMiles(tableRow.getElementsByTag("td").get(4).text().trim());
        }
    }

    public Double getDistanceToLeadVessel() {
        if (getPosition() == 1|| isInStealthMode()) {
            return null;
        }
        else {
            return parseNauticalMiles(tableRow.getElementsByTag("td").get(5).text().trim());
        }
    }

    public Double getDistanceTravelled() {
        if (isInStealthMode()) {
            return null;
        }
        else {
            return parseNauticalMiles(tableRow.getElementsByTag("td").get(6).text().trim());
        }
    }

    public LocalDateTime getTimestamp() {
        String timestampString = tableRow.getElementsByTag("td").get(7).text().trim();
        if (StringUtils.isBlank(timestampString)) {
            return null;
        }
        else {
            return LocalDateTime.parse(timestampString.substring(0, timestampString.length() - 6), TIMESTAMP_PARSER);
        }
    }

    public String getStatus() {
        return tableRow.getElementsByTag("td").get(8).text().trim();
    }

    public LocalDateTime getFinishTime() {
        String finishTimeString = tableRow.getElementsByTag("td").get(9).text().trim();
        if (StringUtils.isBlank(finishTimeString)) {
            return null;
        }
        else {
            return LocalDateTime.parse(finishTimeString.substring(0, finishTimeString.length() - 6), TIMESTAMP_PARSER);
        }
    }

    // TODO need to fix this as it definitely isn't right...
    public boolean isInStealthMode() {
        return tableRow.getElementsByTag("td").get(9).text().trim().equals("stealth");
    }

    private Double parseNauticalMiles(String nauticalMiles) {
        return Double.valueOf(nauticalMiles.substring(0, nauticalMiles.length() - 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RaceStandingsData that = (RaceStandingsData) o;

        return new EqualsBuilder()
                .append(tableRow, that.tableRow)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tableRow)
                .toHashCode();
    }

}
