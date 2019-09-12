package com.surfsoftconsulting.clipper.racetracker.rest;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

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
public class SlackResponse {

    private final String responseType;
    private final String text;
    private final List<SlackResponseAttachment> attachments;

    SlackResponse(String responseType, String text, List<SlackResponseAttachment> attachments) {
        this.responseType = responseType;
        this.text = text;
        this.attachments = attachments;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getText() {
        return text;
    }

    public List<SlackResponseAttachment> getAttachments() {
        return attachments;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("responseType", responseType)
                .append("text", text)
                .append("attachments", attachments)
                .toString();
    }

}
