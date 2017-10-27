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
import com.surfsoftconsulting.clipper.racetracker.slack.PositionResponseRenderer;
import org.springframework.stereotype.Component;

@Component
public class SlackService {

    private final VesselResolver vesselResolver;
    private final PositionResponseRenderer positionResponseRenderer;

    public SlackService(VesselResolver vesselResolver, PositionResponseRenderer positionResponseRenderer) {
        this.vesselResolver = vesselResolver;
        this.positionResponseRenderer = positionResponseRenderer;
    }

    public String getPosition(String text) {
        return positionResponseRenderer.render(text, vesselResolver.resolve(text.trim().toLowerCase()));
    }

}
