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

import com.surfsoftconsulting.clipper.racetracker.rest.ExportedPositionResponse;
import com.surfsoftconsulting.clipper.racetracker.rest.ResponseRenderer;
import com.surfsoftconsulting.clipper.racetracker.service.VesselService;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExportControllerTest {

    private static final String ID = "CV26";
    private static final int RACE_NO = 2;

    private final VesselService vesselService = mock(VesselService.class);
    private final ResponseRenderer csvResponseRenderer = mock(ResponseRenderer.class);

    private final ExportController underTest = new ExportController(vesselService, csvResponseRenderer);

    @Test
    void export() throws IOException {

        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        List<ExportedPositionResponse> export = emptyList();
        when(vesselService.export(ID, RACE_NO)).thenReturn(export);

        underTest.export(response, ID, RACE_NO);

        verify(response).setContentType("text/plain; charset=utf-8");
        verify(csvResponseRenderer).render(export, printWriter);
        verify(printWriter).close();

    }

}