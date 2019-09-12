package com.surfsoftconsulting.clipper.racetracker.rest;

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

import com.surfsoftconsulting.clipper.racetracker.domain.Coordinates;
import com.surfsoftconsulting.clipper.racetracker.domain.Position;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExportedPositionResponseFactoryTest {

    private static final Double LATITUDE = 10.2;
    private static final Double LONGITUDE = 13.1;
    private static final LocalDateTime TIMESTAMP = LocalDateTime.of(2017, 10, 5, 12, 13, 0);
    private static final Integer POSITION = 3;
    private static final Double SPEED = 11.4;
    private static final Integer HEADING = 275;
    private static final Double DISTANCE_REMAINING = 3141.26535;
    private static final Double DISTANCE_TO_LEAD_VESSEL = 102.4;
    private static final Double DISTANCE_TRAVELLED = 204.8;
    private static final String STATUS = "R";
    private static final Coordinates NO_COORDINATES = null;

    private final ExportedPositionResponseFactory underTest = new ExportedPositionResponseFactory();

    @Test
    void withCoordinates() {

        Coordinates coordinates = mock(Coordinates.class);
        when(coordinates.getLatitude()).thenReturn(LATITUDE);
        when(coordinates.getLongitude()).thenReturn(LONGITUDE);
        Position position = mockPosition(coordinates);

        ExportedPositionResponse response = underTest.toExportedPositionResponse(position);

        verifyResponse(response, LATITUDE, LONGITUDE);

    }

    @Test
    void withoutCoordinates() {

        Position position = mockPosition(NO_COORDINATES);

        ExportedPositionResponse response = underTest.toExportedPositionResponse(position);

        verifyResponse(response, null, null);

    }

    private Position mockPosition(Coordinates coordinates) {

        Position position = mock(Position.class);

        when(position.getTimestamp()).thenReturn(TIMESTAMP);
        when(position.getPosition()).thenReturn(POSITION);
        when(position.getCoordinates()).thenReturn(coordinates);
        when(position.getSpeed()).thenReturn(SPEED);
        when(position.getHeading()).thenReturn(HEADING);
        when(position.getDistanceRemaining()).thenReturn(DISTANCE_REMAINING);
        when(position.getDistanceToLeadVessel()).thenReturn(DISTANCE_TO_LEAD_VESSEL);
        when(position.getDistanceTravelled()).thenReturn(DISTANCE_TRAVELLED);
        when(position.getStatus()).thenReturn(STATUS);

        return position;

    }

    private void verifyResponse(ExportedPositionResponse response, Double latitude, Double longitude) {

        assertThat(response.getTimestamp(), is(TIMESTAMP));
        assertThat(response.getPosition(), is(POSITION));
        assertThat(response.getLatitude(), is(latitude));
        assertThat(response.getLongitude(), is(longitude));
        assertThat(response.getSpeed(), is(SPEED));
        assertThat(response.getHeading(), is(HEADING));
        assertThat(response.getDistanceRemaining(), is(DISTANCE_REMAINING));
        assertThat(response.getDistanceToLeadVessel(), is(DISTANCE_TO_LEAD_VESSEL));
        assertThat(response.getDistanceTravelled(), is(DISTANCE_TRAVELLED));
        assertThat(response.getStatus(), is(STATUS));

    }

}