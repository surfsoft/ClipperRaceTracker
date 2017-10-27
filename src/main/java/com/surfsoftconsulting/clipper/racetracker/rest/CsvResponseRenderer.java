package com.surfsoftconsulting.clipper.racetracker.rest;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.List;

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

@Component
public class CsvResponseRenderer implements ResponseRenderer<List<ExportedPositionResponse>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvResponseRenderer.class);

    private static final String FAILED_TO_RENDER_CSV = "Failed to render CSV";

    @Override
    public void render(List<ExportedPositionResponse> data, PrintWriter responseWriter) {

        try {
            StatefulBeanToCsv<ExportedPositionResponse> beanToCsv = new StatefulBeanToCsvBuilder(responseWriter).build();
            beanToCsv.write(data);
        }
        catch(CsvException e) {
            LOGGER.error(FAILED_TO_RENDER_CSV, e);
            throw new RuntimeException(FAILED_TO_RENDER_CSV, e);
        }

    }

}
