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

import com.surfsoftconsulting.clipper.racetracker.service.PositionService;
import com.surfsoftconsulting.clipper.racetracker.web.RaceStandingsData;
import com.surfsoftconsulting.clipper.racetracker.web.SpeedAndCourseData;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PositionFactory {

    private final PositionService positionService;

    public PositionFactory(PositionService positionService) {
        this.positionService = positionService;
    }

    public Position fromRaceStandingsData(String id, RaceStandingsData row, SpeedAndCourseData speedAndCourseData) {

        boolean inStealthMode = row.isInStealthMode();
        LocalDateTime timestamp = row.getTimestamp();

        final int position;
        Coordinates coordinates = null;
        Double speed = null;
        Integer heading = null;
        Double distanceRemaining = null;
        Double distanceToNextPosition = null;
        Double distanceTravelled = null;
        String status = row.getStatus();
        LocalDateTime finishTime = null;

        if (inStealthMode) {
            position = positionService.getPosition(id).getPosition();
        }
        else {
            finishTime = row.getFinishTime();
            position = row.getPosition();
            coordinates = new Coordinates(row.getLatitude(), row.getLongitude());
            if (finishTime == null) {
                if (speedAndCourseData != null) {
                    speed = speedAndCourseData.getSpeed();
                    heading = speedAndCourseData.getHeading();
                }
                distanceRemaining = row.getDistanceRemaining();
                distanceToNextPosition = row.getDistanceToLeadVessel();
            }
            distanceTravelled = row.getDistanceTravelled();
        }
        return new Position(position, coordinates, speed, heading, distanceRemaining, distanceToNextPosition, distanceTravelled, timestamp, status, finishTime, inStealthMode);

    }

}
