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

import com.surfsoftconsulting.clipper.racetracker.domain.Race;
import com.surfsoftconsulting.clipper.racetracker.domain.Vessel;
import com.surfsoftconsulting.clipper.racetracker.domain.VesselRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

@Component
public class RaceService {

    private final VesselRepository vesselRepository;

    public RaceService(VesselRepository vesselRepository) {
        this.vesselRepository = vesselRepository;
    }

    public int getCurrentRace() {

        return vesselRepository
                .findAll()
                .stream()
                .filter(v -> v.getLatestRace().isPresent())
                .map(v -> v.getLatestRace().get())
                .sorted(comparingInt(Race::getRaceNo).reversed())
                .findFirst()
                .map(Race::getRaceNo).orElse(1);

    }

    List<Vessel> getRacePositions(int currentRace) {

        return vesselRepository
                .findAll()
                .stream()
                .filter(v -> v.getRace(currentRace).isPresent())
                .sorted(comparingInt(v -> v.getRace(currentRace).get().getLatestPosition().getPosition()))
                .collect(toList());

    }

}
