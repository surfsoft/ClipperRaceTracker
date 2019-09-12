package com.surfsoftconsulting.clipper.racetracker.rest;

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
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExportedPositionsResponseFactoryTest {

    private final ExportedPositionResponseFactory exportedPositionResponseFactory = mock(ExportedPositionResponseFactory.class);

    private final ExportedPositionsResponseFactory underTest = new ExportedPositionsResponseFactory(exportedPositionResponseFactory);

    @Test
    void toExportedPositionResponse() {

        ExportedPositionResponse response1 = mockResponse();
        Position position1 = mockPosition(response1);
        ExportedPositionResponse response2 = mockResponse();
        Position position2 = mockPosition(response2);
        ExportedPositionResponse response3 = mockResponse();
        Position position3 = mockPosition(response3);

        List<Position> positions = asList(position1, position2, position3);

        List<ExportedPositionResponse> responses = underTest.toExportedPositionResponse(positions);

        assertThat(responses, is(asList(response1, response2, response3)));

    }

    private Position mockPosition(ExportedPositionResponse response) {

        Position position = mock(Position.class);

        when(exportedPositionResponseFactory.toExportedPositionResponse(position)).thenReturn(response);

        return position;

    }

    private ExportedPositionResponse mockResponse() {
        return mock(ExportedPositionResponse.class);
    }

}