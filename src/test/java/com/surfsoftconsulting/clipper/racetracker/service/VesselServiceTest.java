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

import com.sun.tools.javac.util.List;
import com.surfsoftconsulting.clipper.racetracker.domain.*;
import com.surfsoftconsulting.clipper.racetracker.rest.VesselResponse;
import com.surfsoftconsulting.clipper.racetracker.rest.VesselResponseFactory;
import com.surfsoftconsulting.clipper.racetracker.web.RaceStandingsData;
import com.surfsoftconsulting.clipper.racetracker.web.SpeedAndCourseData;
import com.surfsoftconsulting.clipper.racetracker.web.SpeedAndCourseDataResolver;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class VesselServiceTest {

    private static final String VESSEL_NAME = "Boaty McBoatface";
    private static final String VESSEL_ID = "bmb";
    private static final LocalDateTime CURRENT_POSITION_TIMESTAMP = LocalDateTime.of(2017, 8, 29, 14, 30, 0);
    private static final LocalDateTime NEW_POSITION_TIMESTAMP = LocalDateTime.of(2017, 8, 29, 15, 30, 0);

    private final VesselRepository vesselRepository = mock(VesselRepository.class);
    private final VesselFactory vesselFactory = mock(VesselFactory.class);
    private final VesselResponseFactory vesselResponseFactory = mock(VesselResponseFactory.class);
    private final PositionFactory positionFactory = mock(PositionFactory.class);
    private final SpeedAndCourseDataResolver speedAndCourseDataResolver = mock(SpeedAndCourseDataResolver.class);

    private final VesselService underTest = new VesselService(vesselRepository, vesselFactory, vesselResponseFactory, positionFactory, speedAndCourseDataResolver);

    @Test
    void nullId() {
        assertThrows(IllegalArgumentException.class, () -> underTest.create(null, VESSEL_NAME));
    }

    @Test
    void blankId() {
        assertThrows(IllegalArgumentException.class, () -> underTest.create("", VESSEL_NAME));
    }

    @Test
    void nullName() {
        assertThrows(IllegalArgumentException.class, () -> underTest.create(VESSEL_ID, null));
    }

    @Test
    void blankName() {
        assertThrows(IllegalArgumentException.class, () -> underTest.create(VESSEL_ID, ""));
    }

    @Test
    void idExists() {

        Vessel vessel = mock(Vessel.class);
        when(vesselRepository.findById(VESSEL_ID)).thenReturn(vessel);

        assertThat(underTest.create(VESSEL_ID, VESSEL_NAME), is(""));

    }

    @Test
    void vesselCreated() {

        when(vesselRepository.findById(VESSEL_ID)).thenReturn(null);
        Vessel vessel = mock(Vessel.class);
        when(vesselFactory.newVessel(VESSEL_ID, VESSEL_NAME)).thenReturn(vessel);

        assertThat(underTest.create(VESSEL_ID, VESSEL_NAME), is(VESSEL_ID));

        verify(vesselRepository).insert(vessel);

    }

    @Test
    void getVessels() {

        Vessel vessel1 = mock(Vessel.class);
        Vessel vessel2 = mock(Vessel.class);
        when(vesselRepository.findAll()).thenReturn(asList(vessel1, vessel2));
        VesselResponse vesselResponse1 = mock(VesselResponse.class);
        VesselResponse vesselResponse2 = mock(VesselResponse.class);
        when(vesselResponseFactory.toVesselResponse(vessel1)).thenReturn(vesselResponse1);
        when(vesselResponseFactory.toVesselResponse(vessel2)).thenReturn(vesselResponse2);

        assertThat(underTest.getVessels(), is(asList(vesselResponse1, vesselResponse2)));

    }

    @Test
    void updatePositionWhenVesselNotFound() {

        RaceStandingsData raceStandingsData = mock(RaceStandingsData.class);
        when(raceStandingsData.getName()).thenReturn(VESSEL_NAME);
        List<SpeedAndCourseData> speedAndCourseData = mock(List.class);
        when(vesselRepository.findByName(VESSEL_NAME)).thenReturn(null);

        underTest.updatePosition(raceStandingsData, speedAndCourseData);

        verify(vesselRepository, never()).save(any(Vessel.class));

    }

    @Test
    void updatePositionWithFirstPosition() {

        RaceStandingsData raceStandingsData = mock(RaceStandingsData.class);
        when(raceStandingsData.getName()).thenReturn(VESSEL_NAME);
        List<SpeedAndCourseData> speedAndCourseData = mock(List.class);
        Set<Position> positions = mock(Set.class);
        Position currentPosition = mock(Position.class);
        when(currentPosition.getTimestamp()).thenReturn(null);
        Vessel vessel = mock(Vessel.class);
        when(vessel.getId()).thenReturn(VESSEL_ID);
        when(vessel.getName()).thenReturn(VESSEL_NAME);
        when(vessel.getPositions()).thenReturn(positions);
        when(vessel.getLatestPosition()).thenReturn(currentPosition);
        when(vesselRepository.findByName(VESSEL_NAME)).thenReturn(vessel);
        when(speedAndCourseDataResolver.resolve(VESSEL_NAME, speedAndCourseData)).thenReturn(null);
        Position newPosition = mock(Position.class);
        when(positionFactory.fromRaceStandingsRow(VESSEL_ID, raceStandingsData, null)).thenReturn(newPosition);

        underTest.updatePosition(raceStandingsData, speedAndCourseData);

        verify(positions).add(newPosition);
        verify(vesselRepository).save(vessel);

    }

    @Test
    void updatePositionWithsubsequentPosition() {

        RaceStandingsData raceStandingsData = mock(RaceStandingsData.class);
        when(raceStandingsData.getName()).thenReturn(VESSEL_NAME);
        when(raceStandingsData.getTimestamp()).thenReturn(NEW_POSITION_TIMESTAMP);
        List<SpeedAndCourseData> speedAndCourseData = mock(List.class);
        Set<Position> positions = mock(Set.class);
        Position currentPosition = mock(Position.class);
        when(currentPosition.getTimestamp()).thenReturn(CURRENT_POSITION_TIMESTAMP);
        Vessel vessel = mock(Vessel.class);
        when(vessel.getId()).thenReturn(VESSEL_ID);
        when(vessel.getName()).thenReturn(VESSEL_NAME);
        when(vessel.getPositions()).thenReturn(positions);
        when(vessel.getLatestPosition()).thenReturn(currentPosition);
        when(vesselRepository.findByName(VESSEL_NAME)).thenReturn(vessel);
        when(speedAndCourseDataResolver.resolve(VESSEL_NAME, speedAndCourseData)).thenReturn(null);
        Position newPosition = mock(Position.class);
        when(positionFactory.fromRaceStandingsRow(VESSEL_ID, raceStandingsData, null)).thenReturn(newPosition);

        underTest.updatePosition(raceStandingsData, speedAndCourseData);

        verify(positions).add(newPosition);
        verify(vesselRepository).save(vessel);

    }

    @Test
    void updatePositionWhenPositionUnchanged() {

        RaceStandingsData raceStandingsData = mock(RaceStandingsData.class);
        when(raceStandingsData.getName()).thenReturn(VESSEL_NAME);
        when(raceStandingsData.getTimestamp()).thenReturn(CURRENT_POSITION_TIMESTAMP);
        List<SpeedAndCourseData> speedAndCourseData = mock(List.class);
        Position latestPosition = mock(Position.class);
        when(latestPosition.getTimestamp()).thenReturn(CURRENT_POSITION_TIMESTAMP);
        Vessel vessel = mock(Vessel.class);
        when(vesselRepository.findByName(VESSEL_NAME)).thenReturn(vessel);
        when(vessel.getLatestPosition()).thenReturn(latestPosition);

        underTest.updatePosition(raceStandingsData, speedAndCourseData);

        verify(vesselRepository, never()).save(any(Vessel.class));

    }

}