package com.surfsoftconsulting.clipper.racetracker.domain;

import org.junit.jupiter.api.Test;

import static java.util.Collections.emptySet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

class VesselFactoryTest {

    private static final String VESSEL_ID = "bmb";
    private static final String VESSEL_NAME = "Boaty McBoatface";
    private final VesselFactory underTest = new VesselFactory();

    @Test
    void newVessel() {

        Vessel vessel = underTest.newVessel(VESSEL_ID, VESSEL_NAME);

        assertThat(vessel.getId(), is(VESSEL_ID));
        assertThat(vessel.getName(), is(VESSEL_NAME));
        assertThat(vessel.getLatestPosition(), is(new Position()));
        assertThat(vessel.getPositions(), is(emptySet()));

    }

}