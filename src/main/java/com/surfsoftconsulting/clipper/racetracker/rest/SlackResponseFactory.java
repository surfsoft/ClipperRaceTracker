package com.surfsoftconsulting.clipper.racetracker.rest;

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

import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Component
public class SlackResponseFactory {

    private static final String IN_CHANNEL = "in_channel";

    public SlackResponse toSlackResponse(String message) {
        return toSlackResponse(message, emptyList());
    }

    public SlackResponse toSlackResponse(String message, List<String> attachments) {

        List<SlackResponseAttachment> responseAttachments = attachments.stream().map(SlackResponseAttachment::new).collect(toList());

        return new SlackResponse(IN_CHANNEL, message, responseAttachments);

    }

}
