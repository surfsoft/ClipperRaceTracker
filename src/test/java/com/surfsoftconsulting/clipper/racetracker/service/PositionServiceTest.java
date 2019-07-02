package com.surfsoftconsulting.clipper.racetracker.service;

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
import com.surfsoftconsulting.clipper.racetracker.domain.Race;
import com.surfsoftconsulting.clipper.racetracker.domain.Vessel;
import com.surfsoftconsulting.clipper.racetracker.domain.VesselRepository;
import com.surfsoftconsulting.clipper.racetracker.rest.PositionResponse;
import com.surfsoftconsulting.clipper.racetracker.rest.PositionResponseFactory;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PositionServiceTest {

    private static final String VESSEL_ID = "cv21";
    private static final String VESSEL_NAME = "Unicef";
    private static final Integer FLEET_POSITION = 1;

    private final VesselRepository vesselRepository = mock(VesselRepository.class);
    private final Race race = mock(Race.class);
    private final PositionResponseFactory positionResponseFactory = mock(PositionResponseFactory.class);

    private final PositionService underTest = new PositionService(vesselRepository, positionResponseFactory);

    private final Vessel vessel = mock(Vessel.class);
    private final PositionResponse positionResponse = mock(PositionResponse.class);

    @Test
    void nullId() {

        when(positionResponseFactory.toPositionResponse(eq(null), eq(null), any(Position.class), eq(null))).thenReturn(positionResponse);

        assertThat(underTest.getPosition(null), is(positionResponse));

    }

    @Test
    void invalidId() {

        when(positionResponseFactory.toPositionResponse(eq(VESSEL_ID), eq(null), any(Position.class), eq(null))).thenReturn(positionResponse);

        assertThat(underTest.getPosition(VESSEL_ID), is(positionResponse));

    }

    @Test
    void validIdWithNoRace() {

        when(vesselRepository.findById(VESSEL_ID)).thenReturn(Optional.of(vessel));
        when(vessel.getName()).thenReturn(VESSEL_NAME);
        when(vessel.getLatestRace()).thenReturn(Optional.empty());
        when(positionResponseFactory.toPositionResponse(eq(VESSEL_ID), eq(VESSEL_NAME), any(Position.class), eq(null))).thenReturn(positionResponse);

        assertThat(underTest.getPosition(VESSEL_ID), is(positionResponse));

    }

    @Test
    void validIdWithNoPosition() {

        when(vesselRepository.findById(VESSEL_ID)).thenReturn(Optional.of(vessel));
        when(vessel.getName()).thenReturn(VESSEL_NAME);
        when(vessel.getLatestRace()).thenReturn(Optional.of(race));
        Position position = mock(Position.class);
        when(race.getLatestPosition()).thenReturn(position);
        when(race.getFleetPosition()).thenReturn(FLEET_POSITION);
        when(positionResponseFactory.toPositionResponse(VESSEL_ID, VESSEL_NAME, position, FLEET_POSITION)).thenReturn(positionResponse);

        assertThat(underTest.getPosition(VESSEL_ID), is(positionResponse));

    }

}