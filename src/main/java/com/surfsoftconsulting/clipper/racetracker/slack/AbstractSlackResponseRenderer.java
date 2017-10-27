package com.surfsoftconsulting.clipper.racetracker.slack;

import com.surfsoftconsulting.clipper.racetracker.domain.Race;
import com.surfsoftconsulting.clipper.racetracker.domain.Vessel;
import org.apache.commons.lang3.StringUtils;

import static java.lang.String.format;

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

public abstract class AbstractSlackResponseRenderer implements SlackResponseRenderer<Vessel> {

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

    protected abstract String createVesselResponse(String name, Race race);

    private String notFound(String text) {
        if (StringUtils.isBlank(text)) {
            return "Give us a clue - like 'visit seattle' or 'cv26'";
        }
        else {
            return format("You asked for a position report for '%s' but that isn't sufficient to uniquely idenfity one yacht...", text);
        }

    }

    String toPosition(int position) {

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

}
