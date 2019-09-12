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

import com.surfsoftconsulting.clipper.racetracker.service.VesselService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VesselControllerTest {

    private static final String ID = "CV26";
    private static final String NAME = "Visit Seattle";
    private static final String VESSEL_ID = UUID.randomUUID().toString();

    private final VesselService vesselService = mock(VesselService.class);

    private final VesselController underTest = new VesselController(vesselService);

    @Test
    void create() {

        when(vesselService.create(ID, NAME)).thenReturn(VESSEL_ID);

        assertThat(underTest.create(ID, NAME), is(VESSEL_ID));

    }

}