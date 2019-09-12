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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PositionResponseFactoryTest {

    private static final String VESSEL_ID = "CV21";
    private static final int POSITION_1 = 3;
    private static final String VESSEL_NAME = "Unicef";
    private static final String RACING = "r";
    private static final String STEALTH = "s";
    private static final String MAP_VIEW = "https://www.google.com/maps/?q=-15.623037,18.388672";
    private static final Integer POSITION_2 = 2;

    private final CoordinatesResponseFactory coordinatesResponseFactory = mock(CoordinatesResponseFactory.class);
    private final MapViewFactory mapViewFactory = mock(MapViewFactory.class);

    private final PositionResponseFactory underTest = new PositionResponseFactory(coordinatesResponseFactory, mapViewFactory);
    private final Coordinates coordinates = mock(Coordinates.class);
    private final CoordinatesResponse coordinatesResponse = mock(CoordinatesResponse.class);

    @BeforeEach
    void setUp() {
        when(coordinatesResponseFactory.getCoordinatesResponse(coordinates)).thenReturn(coordinatesResponse);
    }

    @Test
    void notInStealthMode() {

        Position position = mockPosition(false);
        when(mapViewFactory.createMapView(ofNullable(coordinates))).thenReturn(MAP_VIEW);

        PositionResponse positionResponse = underTest.toPositionResponse(VESSEL_ID, VESSEL_NAME, position, POSITION_2);

        assertThat(positionResponse.getId(), is(VESSEL_ID));
        assertThat(positionResponse.getName(), is(VESSEL_NAME));
        assertThat(positionResponse.getPosition(), is(POSITION_2));
        assertThat(positionResponse.getMode(), is(RACING));
        assertThat(positionResponse.getCoordinatesResponse(), is(coordinatesResponse));
        assertThat(positionResponse.getMapView(), is(MAP_VIEW));

    }

    @Test
    void inStealthMode() {

        Position position = mockPosition(true);
        when(mapViewFactory.createMapView(empty())).thenReturn(null);

        PositionResponse positionResponse = underTest.toPositionResponse(VESSEL_ID, VESSEL_NAME, position, POSITION_2);

        assertThat(positionResponse.getId(), is(VESSEL_ID));
        assertThat(positionResponse.getName(), is(VESSEL_NAME));
        assertThat(positionResponse.getPosition(), is(POSITION_2));
        assertThat(positionResponse.getMode(), is(STEALTH));
        assertThat(positionResponse.getCoordinatesResponse(), is(nullValue()));
        assertThat(positionResponse.getMapView(), is(nullValue()));

    }

    private Position mockPosition(boolean inStealthMode) {

        Position mockPosition = mock(Position.class);

        when(mockPosition.getPosition()).thenReturn(POSITION_1);
        when(mockPosition.getCoordinates()).thenReturn(inStealthMode ? null : coordinates);
        when(mockPosition.isInStealthMode()).thenReturn(inStealthMode);

        return mockPosition;

    }

}