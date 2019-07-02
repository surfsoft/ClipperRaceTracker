package com.surfsoftconsulting.clipper.racetracker.domain;

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

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
public class VesselResolver {

    private final VesselRepository vesselRepository;

    public VesselResolver(VesselRepository vesselRepository) {
        this.vesselRepository = vesselRepository;
    }

    public Vessel resolve(String text) {

        Optional<Vessel> vessel = vesselRepository.findById(text);
        if (!vessel.isPresent()) {
            List<Vessel> vessels = vesselRepository.findAll().stream().filter(v -> v.getName().toLowerCase().contains(text)).collect(toList());
            if (vessels.size() == 1) {
                vessel = Optional.of(vessels.get(0));
            }
        }

        return (vessel.isPresent()) ? vessel.get() : null;

    }

}
