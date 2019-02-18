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

import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RaceStandingsDataFactoryTest {

    private static final Integer POSITION = 3;
    private static final LocalDateTime TIMESTAMP = LocalDateTime.of(2017, 8, 29, 14, 30, 0);
    private static final Double LATITUDE = Double.valueOf("12.345");
    private static final Double LONGITUDE = Double.valueOf("-3.21");
    private static final Double DISTANCE_REMAINING = Double.valueOf("1043.2");
    private static final Double DISTANCE_TO_LEAD_VESSEL = Double.valueOf("14.32");
    private static final Double DISTANCE_TRAVELLED = Double.valueOf("2143.89");
    private static final String STATUS = "racing";
    private static final LocalDateTime FINISH_TIMESTAMP = LocalDateTime.of(2017, 9, 30, 18, 10, 0);
    private static final Boolean STEALTH_MODE = true;

    private final TableRowParser tableRowParser = mock(TableRowParser.class);

    private final RaceStandingsDataFactory underTest = new RaceStandingsDataFactory(tableRowParser);

    @Test
    void fromElement() {

        Element tableRow = mock(Element.class);
        when(tableRowParser.getPosition(tableRow)).thenReturn(POSITION);
        when(tableRowParser.getLatitude(tableRow)).thenReturn(LATITUDE);
        when(tableRowParser.getLongitude(tableRow)).thenReturn(LONGITUDE);
        when(tableRowParser.getDistanceRemaining(tableRow)).thenReturn(DISTANCE_REMAINING);
        when(tableRowParser.getDistanceToLeadVessel(tableRow)).thenReturn(DISTANCE_TO_LEAD_VESSEL);
        when(tableRowParser.getDistanceTravelled(tableRow)).thenReturn(DISTANCE_TRAVELLED);
        when(tableRowParser.getTimestamp(tableRow)).thenReturn(TIMESTAMP);
        when(tableRowParser.getStatus(tableRow)).thenReturn(STATUS);
        when(tableRowParser.getFinishTime(tableRow)).thenReturn(FINISH_TIMESTAMP);
        when(tableRowParser.isInStealthMode(tableRow)).thenReturn(STEALTH_MODE);

        RaceStandingsData raceStandingsData = underTest.fromTableRow(tableRow);

        assertThat(raceStandingsData.getPosition(), is(POSITION));
        assertThat(raceStandingsData.getLatitude(), is(LATITUDE));
        assertThat(raceStandingsData.getLongitude(), is(LONGITUDE));
        assertThat(raceStandingsData.getDistanceRemaining(), is(DISTANCE_REMAINING));
        assertThat(raceStandingsData.getDistanceToLeadVessel(), is(DISTANCE_TO_LEAD_VESSEL));
        assertThat(raceStandingsData.getDistanceTravelled(), is(DISTANCE_TRAVELLED));
        assertThat(raceStandingsData.getTimestamp(), is(TIMESTAMP));
        assertThat(raceStandingsData.getStatus(), is(STATUS));
        assertThat(raceStandingsData.getFinishTimestamp(), is(FINISH_TIMESTAMP));
        assertThat(raceStandingsData.isInStealthMode(), is(STEALTH_MODE));

    }

}