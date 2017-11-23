package com.surfsoftconsulting.clipper.racetracker.rest;

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

import com.surfsoftconsulting.clipper.racetracker.domain.Position;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PositionResponseFactoryTest {

    private static final String VESSEL_ID = "CV21";
    private static final int POSITION = 3;
    private static final String VESSEL_NAME = "Unicef";
    private static final String RACING = "r";
    private static final String STEALTH = "s";

    private final PositionResponseFactory underTest = new PositionResponseFactory();

    @Test
    void notInStealthMode() {

        Position position = mockPosition(false);

        PositionResponse positionResponse = underTest.toPositionResponse(VESSEL_ID, VESSEL_NAME, position);

        assertThat(positionResponse.getId(), is(VESSEL_ID));
        assertThat(positionResponse.getName(), is(VESSEL_NAME));
        assertThat(positionResponse.getPosition(), is(POSITION));
        assertThat(positionResponse.getMode(), is(RACING));

    }

    @Test
    void inStealthMode() {

        Position position = mockPosition(true);

        PositionResponse positionResponse = underTest.toPositionResponse(VESSEL_ID, VESSEL_NAME, position);

        assertThat(positionResponse.getId(), is(VESSEL_ID));
        assertThat(positionResponse.getName(), is(VESSEL_NAME));
        assertThat(positionResponse.getPosition(), is(POSITION));
        assertThat(positionResponse.getMode(), is(STEALTH));

    }

    private Position mockPosition(boolean inStealthMode) {

        Position mockPosition = mock(Position.class);

        when(mockPosition.getPosition()).thenReturn(POSITION);
        when(mockPosition.isInStealthMode()).thenReturn(inStealthMode);

        return mockPosition;

    }

}