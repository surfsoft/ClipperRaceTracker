package com.surfsoftconsulting.clipper.racetracker.controller;

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

import com.surfsoftconsulting.clipper.racetracker.rest.PositionResponse;
import com.surfsoftconsulting.clipper.racetracker.service.PositionService;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PositionControllerTest {

    private static final String ID = "CV26";

    private final PositionService positionService = mock(PositionService.class);

    private final PositionController underTest = new PositionController(positionService);

    @Test
    void getPosition() {

        PositionResponse positionResponse = mock(PositionResponse.class);
        when(positionService.getPosition(ID)).thenReturn(positionResponse);

        assertThat(underTest.getPosition(ID), is(positionResponse));

    }


}