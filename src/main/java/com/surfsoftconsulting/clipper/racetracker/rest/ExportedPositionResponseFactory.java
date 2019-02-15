package com.surfsoftconsulting.clipper.racetracker.rest;

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

import com.surfsoftconsulting.clipper.racetracker.domain.Position;
import org.springframework.stereotype.Component;

@Component
public class ExportedPositionResponseFactory {

    ExportedPositionResponse toExportedPositionResponse(Position position) {
        return new ExportedPositionResponse(position.getTimestamp(),
                position.getPosition(),
                position.getCoordinates() != null ? position.getCoordinates().getLatitude() : null,
                position.getCoordinates() != null ? position.getCoordinates().getLongitude() : null,
                position.getSpeed(),
                position.getHeading(),
                position.getDistanceRemaining(),
                position.getDistanceToLeadVessel(),
                position.getDistanceTravelled(),
                position.getStatus());
    }

}
