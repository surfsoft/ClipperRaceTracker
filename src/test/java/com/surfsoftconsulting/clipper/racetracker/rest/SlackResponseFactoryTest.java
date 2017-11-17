package com.surfsoftconsulting.clipper.racetracker.rest;

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

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SlackResponseFactoryTest {

    private static final String IN_CHANNEL = "in_channel";
    private static final String MESSAGE = "This is the message";
    private static final String ATTACHMENT_1 = "attachment 1";
    private static final String ATTACHMENT_2 = "attachment 2";

    private final SlackResponseFactory underTest = new SlackResponseFactory();

    @Test
    void messageOnly() {

        SlackResponse slackResponse = underTest.toSlackResponse(MESSAGE);

        assertThat(slackResponse.getResponseType(), is(IN_CHANNEL));
        assertThat(slackResponse.getText(), is(MESSAGE));
        assertThat(slackResponse.getAttachments().isEmpty(), is(true));

    }

    @Test
    void messageWithAttachments() {

        SlackResponse slackResponse = underTest.toSlackResponse(MESSAGE, asList(ATTACHMENT_1, ATTACHMENT_2));

        assertThat(slackResponse.getResponseType(), is(IN_CHANNEL));
        assertThat(slackResponse.getText(), is(MESSAGE));
        assertThat(slackResponse.getAttachments().size(), is(2));
        assertThat(slackResponse.getAttachments().get(0).getText(), is(ATTACHMENT_1));
        assertThat(slackResponse.getAttachments().get(1).getText(), is(ATTACHMENT_2));

    }

}