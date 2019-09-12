package com.surfsoftconsulting.clipper.racetracker.web;

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

import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class RaceStandingsDataFactory {

    private final TableRowParser tableRowParser;

    public RaceStandingsDataFactory(TableRowParser tableRowParser) {
        this.tableRowParser = tableRowParser;
    }

    RaceStandingsData fromTableRow(Element element) {

        return new RaceStandingsData(
                tableRowParser.getPosition(element),
                tableRowParser.getName(element),
                tableRowParser.getLatitude(element),
                tableRowParser.getLongitude(element),
                tableRowParser.getDistanceRemaining(element),
                tableRowParser.getDistanceToLeadVessel(element),
                tableRowParser.getDistanceTravelled(element),
                tableRowParser.getTimestamp(element),
                tableRowParser.getStatus(element),
                tableRowParser.getFinishTime(element),
                tableRowParser.isInStealthMode(element));

    }

}
