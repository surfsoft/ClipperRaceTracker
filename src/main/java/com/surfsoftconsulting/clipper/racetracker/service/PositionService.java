package com.surfsoftconsulting.clipper.racetracker.service;

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
import com.surfsoftconsulting.clipper.racetracker.domain.Race;
import com.surfsoftconsulting.clipper.racetracker.domain.Vessel;
import com.surfsoftconsulting.clipper.racetracker.domain.VesselRepository;
import com.surfsoftconsulting.clipper.racetracker.rest.PositionResponse;
import com.surfsoftconsulting.clipper.racetracker.rest.PositionResponseFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PositionService {

    private final Logger LOGGER = LoggerFactory.getLogger(PositionService.class);

    private final VesselRepository vesselRepository;

    private final PositionResponseFactory positionResponseFactory;

    public PositionService(VesselRepository vesselRepository, PositionResponseFactory positionResponseFactory) {
        this.vesselRepository = vesselRepository;
        this.positionResponseFactory = positionResponseFactory;
    }

    public PositionResponse getPosition(String id) {

        Position position = new Position();
        String name = null;
        Integer fleetPosition = null;
        if (StringUtils.isNotBlank(id)) {
            Optional<Vessel> vessel = vesselRepository.findById(id);
            if (vessel.isPresent()) {
                name = vessel.get().getName();
                Optional<Race> race = vessel.get().getLatestRace();
                if (race.isPresent()) {
                    position = race.get().getLatestPosition();
                    fleetPosition = race.get().getFleetPosition();
                }
            }
            else {
                LOGGER.warn("Vessel with id '{}' not found", id);
            }
        }
        else {
            LOGGER.warn("id is blank");
        }

        return positionResponseFactory.toPositionResponse(id, name, position, fleetPosition);

    }

}
