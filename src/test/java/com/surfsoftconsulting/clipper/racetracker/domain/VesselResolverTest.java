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

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;

class VesselResolverTest {

    private static final String VESSEL_ID = "cv26";
    private static final String VESSEL_NAME_1 = "Visit Seattle";
    private static final String VESSEL_NAME_2 = "Unicef";
    private static final String VESSEL_NAME_3 = "Greenings";
    private static final String PARTIAL_VESSEL_NAME = "uni";
    private static final String UNKNOWN_VESSEL = "Great Britain";
    private static final String AMBIGUOUS_VESSEL_NAME = "e";

    private final VesselRepository vesselRepository = mock(VesselRepository.class);

    private final VesselResolver underTest = new VesselResolver(vesselRepository);

    private final Vessel vessel1 = mockVessel(VESSEL_NAME_1);
    private final Vessel vessel2 = mockVessel(VESSEL_NAME_2);
    private final Vessel vessel3 = mockVessel(VESSEL_NAME_3);

    @Test
    void resolveById() {

        when(vesselRepository.findById(VESSEL_ID)).thenReturn(Optional.of(vessel2));

        assertThat(underTest.resolve(VESSEL_ID), is(vessel2));

    }

    @Test
    void resolveByName() {

        when(vesselRepository.findById(VESSEL_ID)).thenReturn(null);
        when(vesselRepository.findAll()).thenReturn(asList(vessel1, vessel2, vessel3));

        assertThat(underTest.resolve(PARTIAL_VESSEL_NAME), is(vessel2));

    }

    @Test
    void resolvesToNothing() {

        when(vesselRepository.findById(VESSEL_ID)).thenReturn(Optional.empty());
        when(vesselRepository.findAll()).thenReturn(asList(vessel1, vessel2, vessel3));

        assertThat(underTest.resolve(UNKNOWN_VESSEL), is(nullValue()));

    }

    @Test
    void resolvesToMoreThanOne() {

        when(vesselRepository.findById(VESSEL_ID)).thenReturn(Optional.empty());
        when(vesselRepository.findAll()).thenReturn(asList(vessel1, vessel2, vessel3));

        assertThat(underTest.resolve(AMBIGUOUS_VESSEL_NAME), is(nullValue()));

    }

    private Vessel mockVessel(String name) {

        Vessel vessel = mock(Vessel.class);
        when(vessel.getName()).thenReturn(name);

        return vessel;

    }

}