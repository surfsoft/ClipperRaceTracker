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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/export")
public class ExportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportController.class);

    private static final String FAILED_TO_GET_RESPONSE_WRITER = "Failed to get response writer";

    private final VesselService vesselService;
    private final ResponseRenderer<List<ExportedPositionResponse>> csvResponseRenderer;

    public ExportController(VesselService vesselService, ResponseRenderer<List<ExportedPositionResponse>> csvResponseRenderer) {
        this.vesselService = vesselService;
        this.csvResponseRenderer = csvResponseRenderer;
    }

    @RequestMapping(method=GET, path="/{id}/{raceNo}")
    public void export(HttpServletResponse response,
                         @PathVariable("id") String id,
                         @PathVariable("raceNo") int raceNo) {

        response.setContentType("text/plain; charset=utf-8");

        try {
            PrintWriter writer = response.getWriter();
            csvResponseRenderer.render(vesselService.export(id, raceNo), writer);
            writer.close();
        }
        catch(IOException e) {
            LOGGER.error(FAILED_TO_GET_RESPONSE_WRITER, e);
            throw new RuntimeException(FAILED_TO_GET_RESPONSE_WRITER, e);
        }

    }

}
