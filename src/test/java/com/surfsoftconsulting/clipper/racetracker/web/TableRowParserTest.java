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
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class TableRowParserTest {

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
                    "    <td class=\"hide_tab hide_mob\"><span>04 October 2017 11.02 (UTC)</span></td>" +
                    "</tr></table></body></html>";

    private static final String FIFTH_PLACE_HTML =
            "<html><body><table><tr>" +
                    "    <td class=\"min20 marker\"><span class=\"posflag\"><span class=\"colour\" style=\"background-color: #ed1968\"></span><span class=\"num\"><span>5</span></span></span></td>" +
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
                    "    <td class=\"hide_mob\"></td>" +
                    "    <td class=\"hide_port_mob\">racing</td>" +
                    "    <td class=\"hide_tab hide_mob\"><span class=\"nada\"></span></td>" +
                    "</tr></table></body></html>";

    private static final String STEALTH_MODE_HTML =
            "<html><body><table><tr class=\"stealthrow\">" +
                    "    <td class=\"min20 marker\"><span class=\"posflag\"><span class=\"colour\" style=\"background-color: #ed1968\"></span><span class=\"num\"><span>-</span></span></span></td>" +
                    "    <td>" +
                    "        <a href=\"http://clipperroundtheworld.com/team/liverpool-2018\" class=\"cell-inner team-name\" rel=\"noreferrer\">" +
                    "            Liverpool 2018" +
                    "        </a>" +
                    "    </td>" +
                    "    <td class=\"hide_tab hide_mob\"><span class=\"nada\"></span></td>" +
                    "    <td class=\"hide_tab  hide_mob\"><span class=\"nada\"></span></td>" +
                    "    <td><span class=\"nada\"></span></td>" +
                    "    <td class=\"hide_mob\"><span class=\"nada\"></span></td>" +
                    "    <td class=\"hide_mob\"><span class=\"nada\"></span></td>" +
                    "    <td class=\"hide_mob\">FROM: 15 October 2017 06.10 (UTC)<br>UNTIL: 16 October 2017 05.59</td>" +
                    "    <td class=\"hide_port_mob\">Stealth</td>" +
                    "    <td class=\"hide_tab hide_mob\"><span class=\"nada\"></span></td>" +
                    "</tr></table></body></html>";

    private final Element firstPlace = Jsoup.parse(FIRST_PLACE_HTML).body().getElementsByTag("tr").first();
    private final Element fourthPlace = Jsoup.parse(FOURTH_PLACE_HTML).body().getElementsByTag("tr").first();
    private final Element fifthPlace = Jsoup.parse(FIFTH_PLACE_HTML).body().getElementsByTag("tr").first();
    private final Element stealthMode = Jsoup.parse(STEALTH_MODE_HTML).body().getElementsByTag("tr").first();

    private final TableRowParser underTest = new TableRowParser();

    @Test
    void getPosition() {
        assertThat(underTest.getPosition(fourthPlace), is(4));
    }

    @Test
    void getName() {
        assertThat(underTest.getName(fourthPlace), is("Liverpool 2018"));
    }

    @Test
    void getLatitude() {
        assertThat(underTest.getLatitude(fourthPlace), is(Double.valueOf("-36.25152")));
    }

    @Test
    void getLatitudeInStealthMode() {
        assertThat(underTest.getLatitude(stealthMode), is(nullValue()));
    }

    @Test
    void getLongitude() {
        assertThat(underTest.getLongitude(fourthPlace), is(Double.valueOf("-50.83488")));
    }

    @Test
    void getLongitudeInStealthMode() {
        assertThat(underTest.getLongitude(stealthMode), is(nullValue()));
    }

    @Test
    void getDistanceRemaining() {
        assertThat(underTest.getDistanceRemaining(fourthPlace), is(Double.valueOf("3329.46")));
    }

    @Test
    void getDistanceRemainingInStealthMode() {
        assertThat(underTest.getDistanceRemaining(stealthMode), is(nullValue()));
    }

    @Test
    void getDistanceToNextPosition() {
        assertThat(underTest.getDistanceToLeadVessel(fourthPlace), is(Double.valueOf("0.5")));
    }

    @Test
    void getDistanceToNextPositionWhenFirst() {
        assertThat(underTest.getDistanceToLeadVessel(firstPlace), is(nullValue()));
    }

    @Test
    void getDistanceToNextPositionInStealthMode() {
        assertThat(underTest.getDistanceToLeadVessel(stealthMode), is(nullValue()));
    }

    @Test
    void getDistanceTravelled() {
        assertThat(underTest.getDistanceTravelled(fourthPlace), is(Double.valueOf("126")));
    }

    @Test
    void getDistanceTravelledInStealthMode() {
        assertThat(underTest.getDistanceTravelled(stealthMode), is(nullValue()));
    }

    @Test
    void getTimestamp() {
        assertThat(underTest.getTimestamp(fourthPlace), is(LocalDateTime.of(2017, 10, 5, 12, 5, 0)));
    }

    @Test
    void getTimestampWhenBlank() {
        assertThat(underTest.getTimestamp(fifthPlace), is(nullValue()));
    }

    @Test
    void getTimestampInStealthMode() {
        assertThat(underTest.getTimestamp(stealthMode), is(LocalDateTime.of(2017, 10, 16, 5, 59, 0)));
    }

    @Test
    void getFinishTime() {
        assertThat(underTest.getFinishTime(fourthPlace), is(LocalDateTime.of(2017, 10, 4, 11, 2, 0)));
    }

    @Test
    void getFinishTimeWhenBlank() {
        assertThat(underTest.getFinishTime(fifthPlace), is(nullValue()));
    }

    @Test
    void getStatus() {
        assertThat(underTest.getStatus(fourthPlace), is("racing"));
    }

    @Test
    void isNotInStealthMode() {
        assertThat(underTest.isInStealthMode(fourthPlace), is(false));
    }

    @Test
    void isInStealthMode() {
        assertThat(underTest.isInStealthMode(stealthMode), is(true));
    }

}