package com.surfsoftconsulting.clipper.racetracker.service;

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

import com.surfsoftconsulting.clipper.racetracker.domain.Position;
import com.surfsoftconsulting.clipper.racetracker.domain.Race;
import com.surfsoftconsulting.clipper.racetracker.domain.Vessel;
import com.surfsoftconsulting.clipper.racetracker.domain.VesselRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.ImmutableMap.Builder;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RaceServiceTest {

    private static final int RACE_NO = 2;

    private final VesselRepository vesselRepository = mock(VesselRepository.class);

    private final RaceService underTest = new RaceService(vesselRepository);

    private final Vessel vessel1 = mockVessel(new Builder().put(1, 3).put(2, 4).put(3, 1).build());
    private final Vessel vessel2 = mockVessel(new Builder().put(1, 2).put(2, 1).put(3, 2).build());

    @BeforeEach
    void setUp() {
        when(vesselRepository.findAll()).thenReturn(asList(vessel1, vessel2));
    }

    @Test
    void getRacePositions() {
        assertThat(underTest.getRacePositions(1), is(asList(vessel2, vessel1)));
    }

    @Test
    void getCurrentRace() {
        assertThat(underTest.getCurrentRace(), is(3));
    }

    @Test
    void isFinished() {

        Race vessel1Race = mock(Race.class);
        when(vessel1Race.isFinished()).thenReturn(true);
        when(vessel1Race.getFinishTime()).thenReturn(LocalDateTime.now().minusHours(25));
        when(vessel1.getRace(RACE_NO)).thenReturn(Optional.of(vessel1Race));
        Race vessel2Race = mock(Race.class);
        when(vessel2Race.isFinished()).thenReturn(true);
        when(vessel2Race.getFinishTime()).thenReturn(LocalDateTime.now().minusHours(25));
        when(vessel2.getRace(RACE_NO)).thenReturn(Optional.of(vessel2Race));

        assertThat(underTest.isFinished(RACE_NO), is(true));

    }

    @Test
    void oneVesselStillInsideUpdateWindow() {

        Race vessel1Race = mock(Race.class);
        when(vessel1Race.isFinished()).thenReturn(true);
        when(vessel1Race.getFinishTime()).thenReturn(LocalDateTime.now().minusHours(25));
        when(vessel1.getRace(RACE_NO)).thenReturn(Optional.of(vessel1Race));
        Race vessel2Race = mock(Race.class);
        when(vessel2Race.isFinished()).thenReturn(true);
        when(vessel2Race.getFinishTime()).thenReturn(LocalDateTime.now().minusHours(23));
        when(vessel2.getRace(RACE_NO)).thenReturn(Optional.of(vessel2Race));

        assertThat(underTest.isFinished(RACE_NO), is(false));

    }

    @Test
    void oneVesselStillRacing() {

        Race vessel1Race = mock(Race.class);
        when(vessel1Race.isFinished()).thenReturn(true);
        when(vessel1Race.getFinishTime()).thenReturn(LocalDateTime.now().minusHours(25));
        when(vessel1.getRace(RACE_NO)).thenReturn(Optional.of(vessel1Race));
        Race vessel2Race = mock(Race.class);
        when(vessel2Race.isFinished()).thenReturn(false);
        when(vessel2.getRace(RACE_NO)).thenReturn(Optional.of(vessel2Race));

        assertThat(underTest.isFinished(RACE_NO), is(false));

    }

    private Vessel mockVessel(Map<Integer, Integer> seedData) {

        Vessel vessel = mock(Vessel.class);

        List<Race> races = seedData.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map(r -> {
                    Race race = mock(Race.class);
                    Position position = mock(Position.class);
                    when(position.getPosition()).thenReturn(r.getValue());
                    when(race.getLatestPosition()).thenReturn(position);
                    when(race.getRaceNo()).thenReturn(r.getKey());
                    when(vessel.getRace(r.getKey())).thenReturn(Optional.of(race));
                    return race;
                })
                .collect(toList());

        when(vessel.getLatestRace()).thenReturn(Optional.of(races.get(races.size() - 1)));

        return vessel;

    }

}