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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @RequestMapping(method=GET, path="/position/{id}")
    public PositionResponse getPosition(@PathVariable("id") String id) {
        return positionService.getPosition(id);
    }

    @RequestMapping(method=POST, path="/slack")
    public String postSlackPosition(HttpServletRequest request) {

        PositionResponse position = positionService.getPosition(request.getParameter("text"));

        if (position.getName() == null) {
            return String.format("Vessel with id &s could not be located");
        }
        else {
            return String.format("%s (%s) is currently %s", position.getName(), position.getId(), position.getPosition());
        }

    }

}
