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
import com.surfsoftconsulting.clipper.racetracker.domain.Vessel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.lang.String.format;

@Component
public class PositionResponseRenderer {

    public String render(String text, Vessel vessel) {

        final String response;
        if (vessel == null) {
            response = notFound(text);
        }
        else if (vessel.hasNotStarted()) {
            response = "It looks like the Clipper Round the World race hasn't started yet (or at least nobody has told me about it if it has)";
        }
        else {
            response = createVesselResponse(vessel.getName(), vessel.getLatestRace().get());
        }

        return response;

    }

    /**
     * @param text
     * @return
     */
    private String notFound(String text) {
        if (StringUtils.isBlank(text)) {
            return "Give us a clue - like 'visit seattle' or 'cv26'";
        }
        else {
            return format("You asked for a position report for '%s' but that isn't sufficient to uniquely idenfity one yacht...", text);
        }

    }

    private String toPosition(int position) {

        final String suffix;
        switch(position) {
            case 1:
                suffix = "st";
                break;

            case 2:
                suffix = "nd";
                break;

            case 3:
                suffix = "rd";
                break;

            default:
                suffix = "th";
                break;
        }

        return format("%s%s", position, suffix);

    }

    private String createVesselResponse(String name, Race race) {

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
