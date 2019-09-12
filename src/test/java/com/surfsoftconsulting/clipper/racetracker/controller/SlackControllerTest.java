package com.surfsoftconsulting.clipper.racetracker.controller;

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

import com.surfsoftconsulting.clipper.racetracker.rest.SlackResponse;
import com.surfsoftconsulting.clipper.racetracker.service.SlackService;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SlackControllerTest {

    private static final String ID = "CV26";

    private final SlackService slackService = mock(SlackService.class);

    private final SlackController underTest = new SlackController(slackService);

    @Test
    void getSlackPosition() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("text")).thenReturn(ID);
        SlackResponse slackResponse = mock(SlackResponse.class);
        when(slackService.getRaceUpdate(ID)).thenReturn(slackResponse);

        assertThat(underTest.getSlackPosition(request), is(slackResponse));

    }

}