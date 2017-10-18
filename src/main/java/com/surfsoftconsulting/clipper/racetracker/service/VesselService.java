package com.surfsoftconsulting.clipper.racetracker.service;

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

import com.surfsoftconsulting.clipper.racetracker.domain.*;
import com.surfsoftconsulting.clipper.racetracker.rest.VesselResponse;
import com.surfsoftconsulting.clipper.racetracker.rest.VesselResponseFactory;
import com.surfsoftconsulting.clipper.racetracker.web.RaceStandingsData;
import com.surfsoftconsulting.clipper.racetracker.web.SpeedAndCourseData;
import com.surfsoftconsulting.clipper.racetracker.web.SpeedAndCourseDataResolver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class VesselService {

    public static final String ID_OR_NAME_IS_BLANK = "id or name is blank";
    private final Logger LOGGER = LoggerFactory.getLogger(VesselService.class);

    private final VesselRepository vesselRepository;
    private final VesselFactory vesselFactory;
    private final RaceFactory raceFactory;
    private final VesselResponseFactory vesselResponseFactory;
    private final PositionFactory positionFactory;
    private final SpeedAndCourseDataResolver speedAndCourseDataResolver;

    public VesselService(VesselRepository vesselRepository, VesselFactory vesselFactory, RaceFactory raceFactory, VesselResponseFactory vesselResponseFactory, PositionFactory positionFactory, SpeedAndCourseDataResolver speedAndCourseDataResolver) {
        this.vesselRepository = vesselRepository;
        this.vesselFactory = vesselFactory;
        this.raceFactory = raceFactory;
        this.vesselResponseFactory = vesselResponseFactory;
        this.positionFactory = positionFactory;
        this.speedAndCourseDataResolver = speedAndCourseDataResolver;
    }

    public List<VesselResponse> getVessels() {
        return vesselRepository.findAll().stream().map(vesselResponseFactory::toVesselResponse).collect(Collectors.toList());
    }

    public String create(String id, String name) {

        String response = "";

        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(name)) {
            if (vesselRepository.findById(id) == null) {
                vesselRepository.insert(vesselFactory.newVessel(id, name));
                response = id;
            }
            else {
                LOGGER.warn("Vessel with id '{}' already exists. Not created.", id);
            }
        }
        else {
            LOGGER.error(ID_OR_NAME_IS_BLANK);
            throw new IllegalArgumentException(ID_OR_NAME_IS_BLANK);
        }

        return response;

    }

    public void updatePosition(int raceNo, RaceStandingsData raceStandingsData, List<SpeedAndCourseData> speedsAndCourses) {

        Vessel vessel = vesselRepository.findByName(raceStandingsData.getName());
        boolean update = false;
        if (vessel != null) {
            Optional<Race> race = vessel.getRace(raceNo);
            if (!race.isPresent()) {
                Race newRace = raceFactory.newRace(raceNo);
                race = Optional.of(newRace);
                vessel.getRaces().add(newRace);
                update = true;
            }
            if (race.get().getFinishTime() == null) {
                if (raceStandingsData.getTimestamp() == null) {
                    race.get().setFinishTime(raceStandingsData.getFinishTime());
                    update = true;
                }
                else {
                    Position latestPosition = race.get().getLatestPosition();
                    if (latestPosition.getTimestamp() == null || latestPosition.getTimestamp().isBefore(raceStandingsData.getTimestamp())) { // A new update is available; save it
                        SpeedAndCourseData speedAndCourseData = speedAndCourseDataResolver.resolve(vessel.getName(), speedsAndCourses);
                        race.get().getPositions().add(positionFactory.fromRaceStandingsData(vessel.getId(), raceStandingsData, speedAndCourseData));
                        update = true;
                    } else {
                        LOGGER.debug("Vessel '{}' has no new data available", raceStandingsData.getName());
                    }
                }
            }
            else {
                LOGGER.debug("Vessel '{}' has completed this race", raceStandingsData.getName());
            }
        }
        else {
            LOGGER.warn("Vessel with name '{}' not found in database", raceStandingsData.getName());
        }

        if (update) {
            vesselRepository.save(vessel);
        }

    }

}
