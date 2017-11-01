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

import com.surfsoftconsulting.clipper.racetracker.domain.VesselResolver;
import com.surfsoftconsulting.clipper.racetracker.rest.SlackResponse;
import com.surfsoftconsulting.clipper.racetracker.rest.SlackResponseFactory;
import com.surfsoftconsulting.clipper.racetracker.slack.PositionResponseRenderer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Component
public class SlackService {

    private final VesselResolver vesselResolver;
    private final RaceService raceService;
    private final PositionResponseRenderer positionResponseRenderer;
    private final SlackResponseFactory slackResponseFactory;

    public SlackService(VesselResolver vesselResolver, RaceService raceService, PositionResponseRenderer positionResponseRenderer, SlackResponseFactory slackResponseFactory) {
        this.vesselResolver = vesselResolver;
        this.raceService = raceService;
        this.positionResponseRenderer = positionResponseRenderer;
        this.slackResponseFactory = slackResponseFactory;
    }

    public SlackResponse getRaceUpdate(String text) {

        if (StringUtils.isBlank(text)) {
            int currentRace = raceService.getCurrentRace();
            String raceHeadline = format("Positions in race %s of the Clipper Round the World Race are:", currentRace);
            List<String> vessels = raceService.getRacePositions(currentRace).stream().map(v -> String.format("%s: %s (%s)", v.getPosition(currentRace), v.getName(), v.getStatus(currentRace))).collect(toList());
            return slackResponseFactory.toSlackResponse(raceHeadline, vessels);
        }
        else {
            return slackResponseFactory.toSlackResponse(positionResponseRenderer.render(text, vesselResolver.resolve(text.trim().toLowerCase())));
        }

    }

}
