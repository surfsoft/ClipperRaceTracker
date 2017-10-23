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

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.surfsoftconsulting.clipper.racetracker.rest.ExportedPositionResponse;
import com.surfsoftconsulting.clipper.racetracker.service.VesselService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/export")
public class ExportController {

    private final VesselService vesselService;

    public ExportController(VesselService vesselService) {
        this.vesselService = vesselService;
    }

    @RequestMapping(method=GET, path="/{id}/{raceNo}")
    public void export(HttpServletResponse response,
                         @PathVariable("id") String id,
                         @PathVariable("raceNo") int raceNo) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        response.setContentType("text/plain; charset=utf-8");
        PrintWriter writer = response.getWriter();
        StatefulBeanToCsv<ExportedPositionResponse> beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
        beanToCsv.write(vesselService.export(id, raceNo));
        response.getWriter().close();

    }

}
