package com.surfsoftconsulting.clipper.racetracker.controller;

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

import com.surfsoftconsulting.clipper.racetracker.rest.VesselResponse;
import com.surfsoftconsulting.clipper.racetracker.service.VesselService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FleetControllerTest {

    private final VesselService vesselService = mock(VesselService.class);

    private final FleetController underTest = new FleetController(vesselService);

    @Test
    void list() {

        VesselResponse vesselResponse1 = mock(VesselResponse.class);
        VesselResponse vesselResponse2 = mock(VesselResponse.class);
        VesselResponse vesselResponse3 = mock(VesselResponse.class);
        List<VesselResponse> responses = asList(vesselResponse1, vesselResponse2, vesselResponse3);

        when(vesselService.getVessels()).thenReturn(responses);

        assertThat(underTest.list(), is(responses));

    }


}