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
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class RaceStandingsDataTest {

    private static final String FIRST_PLACE_HTML =
            "<html><body><table><tr>" +
            "    <td class=\"min20 marker\"><span class=\"posflag\"><span class=\"colour\" style=\"background-color: #ed1968\"></span><span class=\"num\"><span>1</span></span></span></td>" +
            "    <td>" +
            "        <a href=\"http://clipperroundtheworld.com/team/liverpool-2018\" class=\"cell-inner team-name\" rel=\"noreferrer\">" +
            "            Liverpool 2018" +
            "        </a>" +
            "    </td>" +
            "    <td class=\"hide_tab hide_mob\">-36.25152</td>" +
            "    <td class=\"hide_tab  hide_mob\">-50.83488</td>" +
            "    <td>3329.46NM</td>" +
            "    <td class=\"hide_mob\"><span class=\"nada\"></span></td>" +
            "    <td class=\"hide_mob\">126NM</td>" +
            "    <td class=\"hide_mob\">05 October 2017 12.05 (UTC)</td>" +
            "    <td class=\"hide_port_mob\">racing</td>" +
            "    <td class=\"hide_tab hide_mob\"><span class=\"nada\"></span></td>" +
            "</tr></table></body></html>";

    private static final String FOURTH_PLACE_HTML =
            "<html><body><table><tr>" +
            "    <td class=\"min20 marker\"><span class=\"posflag\"><span class=\"colour\" style=\"background-color: #ed1968\"></span><span class=\"num\"><span>4</span></span></span></td>" +
            "    <td>" +
            "        <a href=\"http://clipperroundtheworld.com/team/liverpool-2018\" class=\"cell-inner team-name\" rel=\"noreferrer\">" +
            "            Liverpool 2018" +
            "        </a>" +
            "    </td>" +
            "    <td class=\"hide_tab hide_mob\">-36.25152</td>" +
            "    <td class=\"hide_tab  hide_mob\">-50.83488</td>" +
            "    <td>3329.46NM</td>" +
            "    <td class=\"hide_mob\"><span class=\"nada\">0.5NM</span></td>" +
            "    <td class=\"hide_mob\">126NM</td>" +
            "    <td class=\"hide_mob\">05 October 2017 12.05 (UTC)</td>" +
            "    <td class=\"hide_port_mob\">racing</td>" +
            "    <td class=\"hide_tab hide_mob\"><span class=\"nada\"></span></td>" +
            "</tr></table></body></html>";

    private static final String STEALTH_MODE_HTML =
            "<html><body><table><tr>" +
            "    <td class=\"min20 marker\"><span class=\"posflag\"><span class=\"colour\" style=\"background-color: #ed1968\"></span><span class=\"num\"><span>4</span></span></span></td>" +
            "    <td>" +
            "        <a href=\"http://clipperroundtheworld.com/team/liverpool-2018\" class=\"cell-inner team-name\" rel=\"noreferrer\">" +
            "            Liverpool 2018" +
            "        </a>" +
            "    </td>" +
            "    <td class=\"hide_tab hide_mob\"></td>" +
            "    <td class=\"hide_tab  hide_mob\"></td>" +
            "    <td></td>" +
            "    <td class=\"hide_mob\"><span class=\"nada\"></span></td>" +
            "    <td class=\"hide_mob\"></td>" +
            "    <td class=\"hide_mob\">05 October 2017 12.05 (UTC)</td>" +
            "    <td class=\"hide_port_mob\">racing</td>" +
            "    <td class=\"hide_tab hide_mob\"><span class=\"nada\">stealth</span></td>" +
            "</tr></table></body></html>";

    private final RaceStandingsData firstPlace = new RaceStandingsData(Jsoup.parse(FIRST_PLACE_HTML).body().getElementsByTag("tr").first());
    private final RaceStandingsData fourthPlace = new RaceStandingsData(Jsoup.parse(FOURTH_PLACE_HTML).body().getElementsByTag("tr").first());
    private final RaceStandingsData stealthMode = new RaceStandingsData(Jsoup.parse(STEALTH_MODE_HTML).body().getElementsByTag("tr").first());

    @Test
    void getPosition() {
        assertThat(fourthPlace.getPosition(), is(Integer.valueOf(4)));
    }

    @Test
    void getName() {
        assertThat(fourthPlace.getName(), is("Liverpool 2018"));
    }

    @Test
    void getLatitude() {
        assertThat(fourthPlace.getLatitude(), is(Double.valueOf("-36.25152")));
    }

    @Test
    void getLatitudeInStealthMode() {
        assertThat(stealthMode.getLatitude(), is(nullValue()));
    }

    @Test
    void getLongitude() {
        assertThat(fourthPlace.getLongitude(), is(Double.valueOf("-50.83488")));
    }

    @Test
    void getLongitudeInStealthMode() {
        assertThat(stealthMode.getLongitude(), is(nullValue()));
    }

    @Test
    void getDistanceRemaining() {
        assertThat(fourthPlace.getDistanceRemaining(), is(Double.valueOf("3329.46")));
    }

    @Test
    void getDistanceRemainingInStealthMode() {
        assertThat(stealthMode.getDistanceRemaining(), is(nullValue()));
    }

    @Test
    void getDistanceToNextPosition() {
        assertThat(fourthPlace.getDistanceToLeadVessel(), is(Double.valueOf("0.5")));
    }

    @Test
    void getDistanceToNextPositionWhenFirst() {
        assertThat(firstPlace.getDistanceToLeadVessel(), is(nullValue()));
    }

    @Test
    void getDistanceToNextPositionInStealthMode() {
        assertThat(stealthMode.getDistanceToLeadVessel(), is(nullValue()));
    }

    @Test
    void getDistanceTravelled() {
        assertThat(fourthPlace.getDistanceTravelled(), is(Double.valueOf("126")));
    }

    @Test
    void getDistanceTravelledInStealthMode() {
        assertThat(stealthMode.getDistanceTravelled(), is(nullValue()));
    }

    @Test
    void getTimestamp() {
        assertThat(fourthPlace.getTimestamp(), is(LocalDateTime.of(2017, 10, 5, 12, 5, 0)));
    }

    @Test
    void getStatus() {
        assertThat(fourthPlace.getStatus(), is("racing"));
    }

    @Test
    void isNotInStealthMode() {
        assertThat(fourthPlace.isInStealthMode(), is(false));
    }

    @Test
    void isInStealthMode() {
        assertThat(stealthMode.isInStealthMode(), is(true));
    }

}