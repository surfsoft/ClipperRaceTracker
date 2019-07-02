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

import com.surfsoftconsulting.clipper.racetracker.domain.*;
import com.surfsoftconsulting.clipper.racetracker.rest.ExportedPositionResponse;
import com.surfsoftconsulting.clipper.racetracker.rest.ExportedPositionsResponseFactory;
import com.surfsoftconsulting.clipper.racetracker.rest.VesselResponse;
import com.surfsoftconsulting.clipper.racetracker.rest.VesselResponseFactory;
import com.surfsoftconsulting.clipper.racetracker.web.RaceStandingsData;
import com.surfsoftconsulting.clipper.racetracker.web.SpeedAndCourseData;
import com.surfsoftconsulting.clipper.racetracker.web.SpeedAndCourseDataResolver;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VesselServiceTest {

    private static final int RACE_NO = 1;
    private static final String VESSEL_NAME = "Boaty McBoatface";
    private static final String VESSEL_ID = "bmb";
    private static final LocalDateTime LAST_UPDATE_TIMESTAMP = LocalDateTime.of(2017, 8, 29, 14, 30, 0);
    private static final LocalDateTime NEXT_UPDATE_TIMESTAMP = LocalDateTime.of(2017, 8, 29, 15, 30, 0);

    private final VesselRepository vesselRepository = mock(VesselRepository.class);
    private final VesselFactory vesselFactory = mock(VesselFactory.class);
    private final VesselResponseFactory vesselResponseFactory = mock(VesselResponseFactory.class);
    private final PositionFactory positionFactory = mock(PositionFactory.class);
    private final SpeedAndCourseDataResolver speedAndCourseDataResolver = mock(SpeedAndCourseDataResolver.class);
    private final RaceFactory raceFactory = mock(RaceFactory.class);
    private final ExportedPositionsResponseFactory exportedPositionsResponseFactory = mock(ExportedPositionsResponseFactory.class);
    private final FleetSorter fleetSorter = mock(FleetSorter.class);

    private final VesselService underTest = new VesselService(vesselRepository, vesselFactory, raceFactory, vesselResponseFactory, positionFactory, speedAndCourseDataResolver, exportedPositionsResponseFactory, fleetSorter);

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
        when(vesselRepository.findById(VESSEL_ID)).thenReturn(Optional.of(vessel));

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
        @SuppressWarnings("unchecked")
        List<SpeedAndCourseData> speedsAndCourses = mock(List.class);
        when(vesselRepository.findByName(VESSEL_NAME)).thenReturn(null);

        underTest.updatePosition(RACE_NO, raceStandingsData, speedsAndCourses);

        verify(vesselRepository, never()).save(any(Vessel.class));

    }

    @Test
    void updateWithFirstPosition() {

        RaceStandingsData raceStandingsData = mock(RaceStandingsData.class);
        when(raceStandingsData.getName()).thenReturn(VESSEL_NAME);
        when(raceStandingsData.getTimestamp()).thenReturn(NEXT_UPDATE_TIMESTAMP);
        Vessel vessel = mock(Vessel.class);
        when(vessel.getId()).thenReturn(VESSEL_ID);
        when(vessel.getName()).thenReturn(VESSEL_NAME);
        @SuppressWarnings("unchecked")
        Set<Race> races = mock(Set.class);
        when(vessel.getRaces()).thenReturn(races);
        when(vesselRepository.findByName(VESSEL_NAME)).thenReturn(vessel);
        Race race = mock(Race.class);
        when(vessel.getRace(RACE_NO)).thenReturn(empty());
        when(raceFactory.newRace(RACE_NO)).thenReturn(race);
        Position latestPosition = mock(Position.class);
        when(race.getLatestPosition()).thenReturn(latestPosition);
        when(latestPosition.getTimestamp()).thenReturn(null);
        @SuppressWarnings("unchecked")
        List<SpeedAndCourseData> speedsAndCourses = mock(List.class);
        SpeedAndCourseData speedAndCourseData = mock(SpeedAndCourseData.class);
        when(speedAndCourseDataResolver.resolve(VESSEL_NAME, speedsAndCourses)).thenReturn(speedAndCourseData);
        Position newPosition = mock(Position.class);
        when(positionFactory.fromRaceStandingsData(raceStandingsData, speedAndCourseData)).thenReturn(newPosition);
        @SuppressWarnings("unchecked")
        Set<Position> positions = mock(Set.class);
        when(race.getPositions()).thenReturn(positions);

        underTest.updatePosition(RACE_NO, raceStandingsData, speedsAndCourses);

        verify(races).add(race);
        verify(positions).add(newPosition);
        verify(vesselRepository).save(any(Vessel.class));

    }

    @Test
    void updateWithNewPosition() {

        RaceStandingsData raceStandingsData = mock(RaceStandingsData.class);
        when(raceStandingsData.getName()).thenReturn(VESSEL_NAME);
        when(raceStandingsData.getTimestamp()).thenReturn(NEXT_UPDATE_TIMESTAMP);
        Vessel vessel = mock(Vessel.class);
        when(vessel.getId()).thenReturn(VESSEL_ID);
        when(vessel.getName()).thenReturn(VESSEL_NAME);
        when(vesselRepository.findByName(VESSEL_NAME)).thenReturn(vessel);
        Race race = mock(Race.class);
        when(vessel.getRace(RACE_NO)).thenReturn(Optional.of(race));
        Position latestPosition = mock(Position.class);
        when(race.getLatestPosition()).thenReturn(latestPosition);
        when(latestPosition.getTimestamp()).thenReturn(LAST_UPDATE_TIMESTAMP);
        @SuppressWarnings("unchecked")
        List<SpeedAndCourseData> speedsAndCourses = mock(List.class);
        SpeedAndCourseData speedAndCourseData = mock(SpeedAndCourseData.class);
        when(speedAndCourseDataResolver.resolve(VESSEL_NAME, speedsAndCourses)).thenReturn(speedAndCourseData);
        Position newPosition = mock(Position.class);
        when(positionFactory.fromRaceStandingsData(raceStandingsData, speedAndCourseData)).thenReturn(newPosition);
        @SuppressWarnings("unchecked")
        Set<Position> positions = mock(Set.class);
        when(race.getPositions()).thenReturn(positions);

        underTest.updatePosition(RACE_NO, raceStandingsData, speedsAndCourses);

        verify(positions).add(newPosition);
        verify(vesselRepository).save(any(Vessel.class));

    }

    @Test
    void updateWithUnchangedPosition() {

        RaceStandingsData raceStandingsData = mock(RaceStandingsData.class);
        when(raceStandingsData.getName()).thenReturn(VESSEL_NAME);
        when(raceStandingsData.getTimestamp()).thenReturn(LAST_UPDATE_TIMESTAMP);
        Vessel vessel = mock(Vessel.class);
        when(vessel.getId()).thenReturn(VESSEL_ID);
        when(vessel.getName()).thenReturn(VESSEL_NAME);
        when(vesselRepository.findByName(VESSEL_NAME)).thenReturn(vessel);
        Race race = mock(Race.class);
        when(vessel.getRace(RACE_NO)).thenReturn(Optional.of(race));
        Position latestPosition = mock(Position.class);
        when(race.getLatestPosition()).thenReturn(latestPosition);
        when(latestPosition.getTimestamp()).thenReturn(LAST_UPDATE_TIMESTAMP);
        @SuppressWarnings("unchecked")
        List<SpeedAndCourseData> speedsAndCourses = mock(List.class);
        SpeedAndCourseData speedAndCourseData = mock(SpeedAndCourseData.class);
        when(speedAndCourseDataResolver.resolve(VESSEL_NAME, speedsAndCourses)).thenReturn(speedAndCourseData);
        Position newPosition = mock(Position.class);
        when(positionFactory.fromRaceStandingsData(raceStandingsData, speedAndCourseData)).thenReturn(newPosition);
        @SuppressWarnings("unchecked")
        Set<Position> positions = mock(Set.class);
        when(race.getPositions()).thenReturn(positions);

        underTest.updatePosition(RACE_NO, raceStandingsData, speedsAndCourses);

        verify(positions, never()).add(newPosition);
        verify(vesselRepository, never()).save(any(Vessel.class));

    }

    @Test
    void updateWhenFinished() {

        RaceStandingsData raceStandingsData = mock(RaceStandingsData.class);
        when(raceStandingsData.getName()).thenReturn(VESSEL_NAME);
        when(raceStandingsData.getFinishTimestamp()).thenReturn(NEXT_UPDATE_TIMESTAMP);
        Vessel vessel = mock(Vessel.class);
        when(vessel.getId()).thenReturn(VESSEL_ID);
        when(vessel.getName()).thenReturn(VESSEL_NAME);
        when(vesselRepository.findByName(VESSEL_NAME)).thenReturn(vessel);
        Race race = mock(Race.class);
        when(vessel.getRace(RACE_NO)).thenReturn(Optional.of(race));
        @SuppressWarnings("unchecked")
        List<SpeedAndCourseData> speedsAndCourses = mock(List.class);

        underTest.updatePosition(RACE_NO, raceStandingsData, speedsAndCourses);

        verify(race).setFinishTime(NEXT_UPDATE_TIMESTAMP);
        verify(vesselRepository).save(vessel);

    }

    @Test
    void updateAfterFinishedNoPositionChange() {

        RaceStandingsData raceStandingsData = mock(RaceStandingsData.class);
        when(raceStandingsData.getName()).thenReturn(VESSEL_NAME);
        when(raceStandingsData.getPosition()).thenReturn(2);
        Vessel vessel = mock(Vessel.class);
        when(vessel.getId()).thenReturn(VESSEL_ID);
        when(vessel.getName()).thenReturn(VESSEL_NAME);
        when(vesselRepository.findByName(VESSEL_NAME)).thenReturn(vessel);
        Race race = mock(Race.class);
        when(race.getFinishTime()).thenReturn(LAST_UPDATE_TIMESTAMP);
        Position position = mock(Position.class);
        when(position.getPosition()).thenReturn(2);
        when(race.getLatestPosition()).thenReturn(position);
        when(vessel.getRace(RACE_NO)).thenReturn(Optional.of(race));
        @SuppressWarnings("unchecked")
        List<SpeedAndCourseData> speedsAndCourses = mock(List.class);

        underTest.updatePosition(RACE_NO, raceStandingsData, speedsAndCourses);

        verify(position, never()).setPosition(anyInt());
        verify(vesselRepository, never()).save(any(Vessel.class));

    }

    @Test
    void updateAfterFinishedWhenPositionChanges() {

        RaceStandingsData raceStandingsData = mock(RaceStandingsData.class);
        when(raceStandingsData.getName()).thenReturn(VESSEL_NAME);
        when(raceStandingsData.getPosition()).thenReturn(2);
        Vessel vessel = mock(Vessel.class);
        when(vessel.getId()).thenReturn(VESSEL_ID);
        when(vessel.getName()).thenReturn(VESSEL_NAME);
        when(vesselRepository.findByName(VESSEL_NAME)).thenReturn(vessel);
        Race race = mock(Race.class);
        when(race.getFinishTime()).thenReturn(LAST_UPDATE_TIMESTAMP);
        Position position = mock(Position.class);
        when(position.getPosition()).thenReturn(1);
        when(race.getLatestPosition()).thenReturn(position);
        when(vessel.getRace(RACE_NO)).thenReturn(Optional.of(race));
        @SuppressWarnings("unchecked")
        List<SpeedAndCourseData> speedsAndCourses = mock(List.class);

        underTest.updatePosition(RACE_NO, raceStandingsData, speedsAndCourses);

        verify(position).setPosition(2);
        verify(vesselRepository).save(vessel);

    }

    @Test
    void exportInvalidVesselId() {

        when(vesselRepository.findById(VESSEL_ID)).thenReturn(Optional.empty());

        assertThat(underTest.export(VESSEL_ID, 1), is(emptyList()));

    }

    @Test
    void exportInvalidRaceNo() {

        Vessel vessel = mock(Vessel.class);
        when(vesselRepository.findById(VESSEL_ID)).thenReturn(Optional.of(vessel));
        when(vessel.getRace(RACE_NO)).thenReturn(empty());

        assertThat(underTest.export(VESSEL_ID, RACE_NO), is(emptyList()));

    }

    @Test
    void export() {

        Vessel vessel = mock(Vessel.class);
        when(vesselRepository.findById(VESSEL_ID)).thenReturn(Optional.of(vessel));
        Race race = mock(Race.class);
        when(vessel.getRace(RACE_NO)).thenReturn(Optional.of(race));
        HashSet<Position> positions = new HashSet<>();
        Position position1 = mockPosition(LocalDateTime.of(2017, 10, 20, 2, 2, 3));
        Position position2 = mockPosition(LocalDateTime.of(2017, 10, 20, 1, 2, 3));
        positions.add(position1);
        positions.add(position2);
        when(race.getPositions()).thenReturn(positions);
        List<ExportedPositionResponse> exportedPositions = Collections.emptyList();
        when(exportedPositionsResponseFactory.toExportedPositionResponse(asList(position2, position1))).thenReturn(exportedPositions);

        assertThat(underTest.export(VESSEL_ID, RACE_NO), is(exportedPositions));

    }

    @Test
    void updateFleetPositions() {

        Race vessel1race = mockRace(1);
        Race vessel2race = mockRace(2);
        Race vessel3race = mockRace(2);

        Vessel vessel1 = mockVessel(vessel1race, 1);
        Vessel vessel2 = mockVessel(vessel2race, 2);
        Vessel vessel3 = mockVessel(vessel3race, 2);
        Vessel vessel4 = mockVessel(null, 2);
        List<Vessel> vessels = asList(vessel1, vessel2, vessel3, vessel4);
        when(vesselRepository.findAll()).thenReturn(vessels);

        Comparator<Vessel> fleetPositionComparator = mock(FleetPositionComparator.class);
        when(fleetPositionComparator.compare(any(Vessel.class), any(Vessel.class))).thenReturn(1);

        underTest.updateFleetPositions();

        verify(fleetSorter).sort(vessels, 2);
        verify(vessel1race, never()).setFleetPosition(anyInt());
        verify(vessel2race).setFleetPosition(anyInt());
        verify(vessel3race).setFleetPosition(anyInt());
        verify(vesselRepository).saveAll(vessels);

    }

    private Position mockPosition(LocalDateTime timestamp) {

        Position position = mock(Position.class);
        when(position.getTimestamp()).thenReturn(timestamp);

        return position;

    }

    private Race mockRace(int raceNo) {

        Race race = mock(Race.class);

        when(race.getRaceNo()).thenReturn(raceNo);

        return race;

    }

    private Vessel mockVessel(Race race, int raceNo) {

        Vessel vessel = mock(Vessel.class);

        when(vessel.getLatestRace()).thenReturn(Optional.ofNullable(race));
        when(vessel.getRace(raceNo)).thenReturn(Optional.ofNullable(race));

        return vessel;

    }

}