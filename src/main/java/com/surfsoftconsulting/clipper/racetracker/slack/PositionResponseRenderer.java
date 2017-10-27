package com.surfsoftconsulting.clipper.racetracker.slack;

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
import com.surfsoftconsulting.clipper.racetracker.domain.Race;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.lang.String.format;

@Component
public class PositionResponseRenderer extends AbstractSlackResponseRenderer {

    protected String createVesselResponse(String name, Race race) {

        Position position = race.getLatestPosition();
        LocalDateTime finishTime = race.getFinishTime();

        final String response;
        if (finishTime != null) {
            response = format("%s has finished race %s in %s place", name, race.getRaceNo(), toPosition(position.getPosition()));
        }
        else if (position.isInStealthMode()) {
            response = format("%s is in stealth mode", name);
        }
        else {
            response = format("%s is in %s place", name, toPosition(position.getPosition()));
        }

        return response;

    }

}
