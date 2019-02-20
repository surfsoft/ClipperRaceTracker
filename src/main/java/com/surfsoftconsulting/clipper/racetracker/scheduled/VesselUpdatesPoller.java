package com.surfsoftconsulting.clipper.racetracker.scheduled;

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

import com.surfsoftconsulting.clipper.racetracker.service.RaceService;
import com.surfsoftconsulting.clipper.racetracker.service.VesselService;
import com.surfsoftconsulting.clipper.racetracker.web.*;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VesselUpdatesPoller {

    private final Logger LOGGER = LoggerFactory.getLogger(VesselUpdatesPoller.class);

    private static final String RACE_STANDINGS_PAGE = "https://www.clipperroundtheworld.com/race/standings";

    private final RaceStandingsDocumentFactory raceStandingsDocumentFactory;
    private final RaceStandingsDataParser raceStandingsDataParser;
    private final SpeedAndCourseDataParser speedAndCourseDataParser;
    private final RaceNumberParser raceNumberParser;
    private final RaceService raceService;
    private final VesselService vesselService;

    public VesselUpdatesPoller(RaceStandingsDocumentFactory raceStandingsDocumentFactory,
                               RaceStandingsDataParser raceStandingsDataParser,
                               SpeedAndCourseDataParser speedAndCourseDataParser,
                               RaceNumberParser raceNumberParser,
                               RaceService raceService, VesselService vesselService) {

        this.raceStandingsDocumentFactory = raceStandingsDocumentFactory;
        this.raceStandingsDataParser = raceStandingsDataParser;
        this.speedAndCourseDataParser = speedAndCourseDataParser;
        this.raceNumberParser = raceNumberParser;
        this.raceService = raceService;
        this.vesselService = vesselService;

    }

    @Scheduled(fixedRateString = "${com.surfsoftconsulting.clipper.racetracker.scheduled.poll.interval}")
    public void pollForUpdates() {

        LOGGER.debug("Polling {} for changes", RACE_STANDINGS_PAGE);

        Document standingsPage = raceStandingsDocumentFactory.fromUrl(RACE_STANDINGS_PAGE);

        if (standingsPage != null) {
            int raceNo = raceNumberParser.parse(standingsPage);
            if (!raceService.isFinished(raceNo)) {
                List<SpeedAndCourseData> speedsAndCourses = speedAndCourseDataParser.parse(standingsPage);
                long updateCount = raceStandingsDataParser.parse(standingsPage).stream().map(raceStandingsData -> vesselService.updatePosition(raceNo, raceStandingsData, speedsAndCourses)).count();
                if (updateCount > 0) {
                    vesselService.updateFleetPositions();
                }
            }

        }

    }

}
