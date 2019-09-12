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
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FleetPositionComparatorTest {

    private static final int RACE_NO = 1;
    private static final LocalDateTime OCT_4TH_TIMESTAMP = LocalDateTime.of(2017, 10, 4, 12, 13, 14);
    private static final LocalDateTime OCT_5TH_TIMESTAMP = LocalDateTime.of(2017, 10, 5, 21, 20, 19);

    private final FleetPositionComparator underTest = new FleetPositionComparator(RACE_NO);

    private final Vessel vessel1 = mock(Vessel.class);
    private final Vessel vessel2 = mock(Vessel.class);
    private final Race race1 = mock(Race.class);
    private final Race race2 = mock(Race.class);

    @Test
    void neitherVesselHasRace() {
        assertThat(underTest.compare(vessel1, vessel2), is(0));
    }

    @Test
    void vessel1HasNoRace() {

        when(vessel1.getRace(RACE_NO)).thenReturn(empty());
        when(vessel2.getRace(RACE_NO)).thenReturn(of(race2));

        assertThat(underTest.compare(vessel1, vessel2), is(1));

    }

    @Test
    void vessel2HasNoRace() {

        when(vessel1.getRace(RACE_NO)).thenReturn(of(race1));
        when(vessel2.getRace(RACE_NO)).thenReturn(empty());

        assertThat(underTest.compare(vessel1, vessel2), is(-1));

    }

    @Test
    void bothVesselsFinished() {

        mockFinished(race1, 2);
        mockFinished(race2, 1);

        when(vessel1.getRace(RACE_NO)).thenReturn(of(race1));
        when(vessel2.getRace(RACE_NO)).thenReturn(of(race2));

        assertThat(underTest.compare(vessel1, vessel2), is(1));

    }

    @Test
    void vessel1FinishedVessel2Racing() {

        mockFinished(race1, 1);
        mockRacing(race2, 51.3);
        when(vessel1.getRace(RACE_NO)).thenReturn(of(race1));
        when(vessel2.getRace(RACE_NO)).thenReturn(of(race2));

        assertThat(underTest.compare(vessel1, vessel2), is(-1));

    }

    @Test
    void vessel2FinishedVessel1Racing() {

        mockRacing(race1, 102.2);
        mockFinished(race2, 1);
        when(vessel1.getRace(RACE_NO)).thenReturn(of(race1));
        when(vessel2.getRace(RACE_NO)).thenReturn(of(race2));

        assertThat(underTest.compare(vessel1, vessel2), is(1));

    }

    @Test
    void bothVesselsRacing() {

        mockRacing(race1, 102.2);
        mockRacing(race2, 54.3);
        when(vessel1.getRace(RACE_NO)).thenReturn(of(race1));
        when(vessel2.getRace(RACE_NO)).thenReturn(of(race2));

        assertThat(underTest.compare(vessel1, vessel2), is(1));

    }

    @Test
    void vessel1RacingVessel2InStealth() {

        mockRacing(race1, 102.2);
        mockStealth(race2, OCT_4TH_TIMESTAMP);
        when(vessel1.getRace(RACE_NO)).thenReturn(of(race1));
        when(vessel2.getRace(RACE_NO)).thenReturn(of(race2));

        assertThat(underTest.compare(vessel1, vessel2), is(-1));

    }

    @Test
    void vessel1InStealthVessel2Racing() {

        mockStealth(race1, OCT_4TH_TIMESTAMP);
        mockRacing(race2, 102.2);
        when(vessel1.getRace(RACE_NO)).thenReturn(of(race1));
        when(vessel2.getRace(RACE_NO)).thenReturn(of(race2));

        assertThat(underTest.compare(vessel1, vessel2), is(1));

    }

    @Test
    void bothVesselsInStealth() {

        mockStealth(race1, OCT_5TH_TIMESTAMP);
        mockStealth(race2, OCT_4TH_TIMESTAMP);
        when(vessel1.getRace(RACE_NO)).thenReturn(of(race1));
        when(vessel2.getRace(RACE_NO)).thenReturn(of(race2));

        assertThat(underTest.compare(vessel1, vessel2), is(1));

    }

    private void mockStealth(Race race, LocalDateTime timestamp) {

        Position position = mock(Position.class);
        when(position.isInStealthMode()).thenReturn(true);
        when(position.getTimestamp()).thenReturn(timestamp);

        when(race.isFinished()).thenReturn(false);
        when(race.getLatestPosition()).thenReturn(position);

    }

    private void mockRacing(Race race, double distanceRemaining) {

        Position position = mock(Position.class);
        when(position.isInStealthMode()).thenReturn(false);
        when(position.getDistanceRemaining()).thenReturn(distanceRemaining);

        when(race.isFinished()).thenReturn(false);
        when(race.getLatestPosition()).thenReturn(position);

    }

    private void mockFinished(Race race, int finishPosition) {

        Position position = mock(Position.class);
        when(position.isInStealthMode()).thenReturn(false);
        when(position.getPosition()).thenReturn(finishPosition);

        when(race.isFinished()).thenReturn(true);
        when(race.getLatestPosition()).thenReturn(position);

    }

}