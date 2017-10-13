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

import com.surfsoftconsulting.clipper.racetracker.domain.Vessel;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VesselResponseFactoryTest {

    private static final String VESSEL_ID = "CV21";
    private static final String VESSEL_NAME = "Visit Seattle";


    private final VesselResponseFactory underTest = new VesselResponseFactory();

    @Test
    void toVesselResponse() {

        Vessel vessel = mock(Vessel.class);
        when(vessel.getId()).thenReturn(VESSEL_ID);
        when(vessel.getName()).thenReturn(VESSEL_NAME);

        VesselResponse vesselResponse = underTest.toVesselResponse(vessel);

        assertThat(vesselResponse.getId(), is(VESSEL_ID));
        assertThat(vesselResponse.getName(), is(VESSEL_NAME));

    }

}