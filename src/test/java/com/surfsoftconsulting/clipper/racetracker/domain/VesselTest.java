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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class VesselTest {

    private static final String VESSEL_ID = "bmb";
    private static final String VESSEL_NAME = "Boaty McBoatface";
    private final Race race2 = new Race(2);

    Vessel underTest = new Vessel(VESSEL_ID, VESSEL_NAME);

    private Race race1 = new Race(1);

    @BeforeEach
    void setUp() {

        underTest.getRaces().add(race1);
        underTest.getRaces().add(race2);

    }

    @Test
    void getRace() {

        Optional<Race> race = underTest.getRace(1);

        assertThat(race.isPresent(), is(true));
        assertThat(race.get(), is(race1));

    }

    @Test
    void getMissingRace() {
        assertThat(underTest.getRace(3).isPresent(), is(false));

    }

    @Test
    void getLatestRace() {

        Optional<Race> race = underTest.getLatestRace();

        assertThat(race.isPresent(), is(true));
        assertThat(race.get(), is(race2));

    }


}