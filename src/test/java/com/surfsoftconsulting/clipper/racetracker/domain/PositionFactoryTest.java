package com.surfsoftconsulting.clipper.racetracker.domain;

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

import com.surfsoftconsulting.clipper.racetracker.web.RaceStandingsData;
import com.surfsoftconsulting.clipper.racetracker.web.SpeedAndCourseData;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PositionFactoryTest {

    private static final int POSITION = 3;
    private static final LocalDateTime TIMESTAMP = LocalDateTime.of(2017, 8, 29, 14, 30, 0);
    private static final LocalDateTime FINISH_TIMESTAMP = LocalDateTime.of(2017, 9, 30, 18, 10, 0);
    private static final Double LATITUDE = Double.valueOf("12.345");
    private static final Double LONGITUDE = Double.valueOf("-3.21");
    private static final Double SPEED = Double.valueOf("12.3");
    private static final int HEADING = Integer.parseInt("185");
    private static final Double DISTANCE_REMAINING = Double.valueOf("1043.2");
    private static final Double DISTANCE_TO_LEAD_VESSEL = Double.valueOf("14.32");
    private static final Double DISTANCE_TRAVELLED = Double.valueOf("2143.89");
    private static final String STATUS = "racing";

    private final PositionFactory underTest = new PositionFactory();
    private final RaceStandingsData raceStandingsData = mock(RaceStandingsData.class);
    private final SpeedAndCourseData speedAndCourseData = mock(SpeedAndCourseData.class);

    @Test
    void fromRaceStandingsRowWithSpeedAndCourseDataWhileRacing() {

        when(raceStandingsData.isInStealthMode()).thenReturn(false);
        when(raceStandingsData.getTimestamp()).thenReturn(TIMESTAMP);
        when(raceStandingsData.getFinishTime()).thenReturn(null);
        when(raceStandingsData.getPosition()).thenReturn(POSITION);
        when(raceStandingsData.getLatitude()).thenReturn(LATITUDE);
        when(raceStandingsData.getLongitude()).thenReturn(LONGITUDE);
        when(raceStandingsData.getDistanceRemaining()).thenReturn(DISTANCE_REMAINING);
        when(raceStandingsData.getDistanceToLeadVessel()).thenReturn(DISTANCE_TO_LEAD_VESSEL);
        when(raceStandingsData.getDistanceTravelled()).thenReturn(DISTANCE_TRAVELLED);
        when(raceStandingsData.getStatus()).thenReturn(STATUS);
        when(speedAndCourseData.getSpeed()).thenReturn(SPEED);
        when(speedAndCourseData.getHeading()).thenReturn(HEADING);

        Position position = underTest.fromRaceStandingsData(raceStandingsData, speedAndCourseData);

        assertThat(position.getPosition(), is(POSITION));
        assertThat(position.getCoordinates(), is(new Coordinates(LATITUDE, LONGITUDE)));
        assertThat(position.getSpeed(), is(SPEED));
        assertThat(position.getHeading(), is(HEADING));
        assertThat(position.getDistanceRemaining(), is(DISTANCE_REMAINING));
        assertThat(position.getDistanceToLeadVessel(), is(DISTANCE_TO_LEAD_VESSEL));
        assertThat(position.getDistanceTravelled(), is(DISTANCE_TRAVELLED));
        assertThat(position.getTimestamp(), is(TIMESTAMP));
        assertThat(position.getStatus(), is(STATUS));
        assertThat(position.isInStealthMode(), is(false));

    }

    @Test
    void fromRaceStandingsRowWithoutSpeedAndCourseDataWhileRacing() {

        when(raceStandingsData.isInStealthMode()).thenReturn(false);
        when(raceStandingsData.getTimestamp()).thenReturn(TIMESTAMP);
        when(raceStandingsData.getFinishTime()).thenReturn(null);
        when(raceStandingsData.getPosition()).thenReturn(POSITION);
        when(raceStandingsData.getLatitude()).thenReturn(LATITUDE);
        when(raceStandingsData.getLongitude()).thenReturn(LONGITUDE);
        when(raceStandingsData.getDistanceRemaining()).thenReturn(DISTANCE_REMAINING);
        when(raceStandingsData.getDistanceToLeadVessel()).thenReturn(DISTANCE_TO_LEAD_VESSEL);
        when(raceStandingsData.getDistanceTravelled()).thenReturn(DISTANCE_TRAVELLED);
        when(raceStandingsData.getStatus()).thenReturn(STATUS);

        Position position = underTest.fromRaceStandingsData(raceStandingsData, null);

        assertThat(position.getPosition(), is(POSITION));
        assertThat(position.getCoordinates(), is(new Coordinates(LATITUDE, LONGITUDE)));
        assertThat(position.getSpeed(), is(nullValue()));
        assertThat(position.getHeading(), is(nullValue()));
        assertThat(position.getDistanceRemaining(), is(DISTANCE_REMAINING));
        assertThat(position.getDistanceToLeadVessel(), is(DISTANCE_TO_LEAD_VESSEL));
        assertThat(position.getDistanceTravelled(), is(DISTANCE_TRAVELLED));
        assertThat(position.getTimestamp(), is(TIMESTAMP));
        assertThat(position.getStatus(), is(STATUS));
        assertThat(position.isInStealthMode(), is(false));

    }

    @Test
    void fromRaceStandingsRowInStealthMode() {

        when(raceStandingsData.isInStealthMode()).thenReturn(true);
        when(raceStandingsData.getTimestamp()).thenReturn(TIMESTAMP);
        when(raceStandingsData.getStatus()).thenReturn(STATUS);
        when(raceStandingsData.getPosition()).thenReturn(POSITION);

        Position position = underTest.fromRaceStandingsData(raceStandingsData, null);

        assertThat(position.getPosition(), is(POSITION));
        assertThat(position.getCoordinates(), is(nullValue()));
        assertThat(position.getSpeed(), is(nullValue()));
        assertThat(position.getHeading(), is(nullValue()));
        assertThat(position.getDistanceRemaining(), is(nullValue()));
        assertThat(position.getDistanceToLeadVessel(), is(nullValue()));
        assertThat(position.getDistanceTravelled(), is(nullValue()));
        assertThat(position.getTimestamp(), is(TIMESTAMP));
        assertThat(position.getStatus(), is(STATUS));
        assertThat(position.isInStealthMode(), is(true));

    }

    @Test
    void fromRaceStandingsRowWhenFinished() {

        when(raceStandingsData.isInStealthMode()).thenReturn(false);
        when(raceStandingsData.getTimestamp()).thenReturn(TIMESTAMP);
        when(raceStandingsData.getFinishTime()).thenReturn(FINISH_TIMESTAMP);
        when(raceStandingsData.getPosition()).thenReturn(POSITION);
        when(raceStandingsData.getLatitude()).thenReturn(LATITUDE);
        when(raceStandingsData.getLongitude()).thenReturn(LONGITUDE);
        when(raceStandingsData.getDistanceTravelled()).thenReturn(DISTANCE_TRAVELLED);
        when(raceStandingsData.getStatus()).thenReturn(STATUS);

        Position position = underTest.fromRaceStandingsData(raceStandingsData, null);

        assertThat(position.getPosition(), is(POSITION));
        assertThat(position.getCoordinates(), is(new Coordinates(LATITUDE, LONGITUDE)));
        assertThat(position.getSpeed(), is(nullValue()));
        assertThat(position.getHeading(), is(nullValue()));
        assertThat(position.getDistanceRemaining(), is(nullValue()));
        assertThat(position.getDistanceToLeadVessel(), is(nullValue()));
        assertThat(position.getDistanceTravelled(), is(DISTANCE_TRAVELLED));
        assertThat(position.getTimestamp(), is(TIMESTAMP));
        assertThat(position.getStatus(), is(STATUS));
        assertThat(position.isInStealthMode(), is(false));

    }

}