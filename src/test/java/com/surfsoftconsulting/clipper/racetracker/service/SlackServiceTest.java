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

import com.surfsoftconsulting.clipper.racetracker.domain.Vessel;
import com.surfsoftconsulting.clipper.racetracker.domain.VesselResolver;
import com.surfsoftconsulting.clipper.racetracker.rest.SlackResponse;
import com.surfsoftconsulting.clipper.racetracker.rest.SlackResponseFactory;
import com.surfsoftconsulting.clipper.racetracker.slack.PositionResponseRenderer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SlackServiceTest {

    private static final String VESSEL_TEXT = "cv21";
    private static final String POSITION_RESPONSE = "Somewhere in the mid-atlantic";

    private final VesselResolver vesselResolver = mock(VesselResolver.class);
    private final PositionResponseRenderer positionResponseRenderer = mock(PositionResponseRenderer.class);
    private final SlackResponseFactory slackResponseFactory = mock(SlackResponseFactory.class);
    private final RaceService raceService = mock(RaceService.class);

    private final SlackService underTest = new SlackService(vesselResolver, raceService, positionResponseRenderer, slackResponseFactory);

    @Test
    void getPosition() {

        Vessel vessel = mock(Vessel.class);
        when(vesselResolver.resolve(VESSEL_TEXT)).thenReturn(vessel);
        when(positionResponseRenderer.render(VESSEL_TEXT, vessel)).thenReturn(POSITION_RESPONSE);
        SlackResponse slackResponse = mock(SlackResponse.class);
        when(slackResponseFactory.toSlackResponse(POSITION_RESPONSE)).thenReturn(slackResponse);

        assertThat(underTest.getRaceUpdate(VESSEL_TEXT), is(slackResponse));

    }

}