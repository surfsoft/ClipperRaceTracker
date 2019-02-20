package com.surfsoftconsulting.clipper.racetracker.slack;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PositionResponseRendererTest {

    private final PositionRenderer positionRenderer = mock(PositionRenderer.class);

    private final PositionResponseRenderer underTest = new PositionResponseRenderer(positionRenderer);

    @BeforeEach
    void setUp() {
        when(positionRenderer.toPosition(1)).thenReturn("1st");
    }

    @Test
    void notFinished() {

        Vessel vessel = mockVessel(null, false);

        assertThat(underTest.render(vessel), is("CV26 is in 1st place"));

    }

    @Test
    void finished() {

        LocalDateTime finishTime = LocalDateTime.of(2017, 10, 4, 11, 29, 0);
        Vessel vessel = mockVessel(finishTime, false);

        assertThat(underTest.render(vessel), is("CV26 has finished race 2 in 1st place"));

    }

    @Test
    void stealthMode() {

        Vessel vessel = mockVessel(null, true);

        assertThat(underTest.render(vessel), is("CV26 is in stealth mode"));

    }

    private Vessel mockVessel(LocalDateTime finishTime, boolean stealthMode) {

        Position position = mock(Position.class);
        when(position.isInStealthMode()).thenReturn(stealthMode);

        Race race = mock(Race.class);
        when(race.getLatestPosition()).thenReturn(position);
        when(race.getRaceNo()).thenReturn(2);
        when(race.getFinishTime()).thenReturn(finishTime);
        when(race.getFleetPosition()).thenReturn(1);

        Vessel vessel = mock(Vessel.class);
        when(vessel.getName()).thenReturn("CV26");
        when(vessel.getLatestRace()).thenReturn(Optional.of(race));

        return vessel;

    }

}