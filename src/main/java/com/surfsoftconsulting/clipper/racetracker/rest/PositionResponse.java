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

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PositionResponse {

    private final String id;
    private final String name;
    private final int position;
    private final String mode;

    PositionResponse(String id, String name, int position, String mode) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.mode = mode;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public String getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("position", position)
                .append("mode", mode)
                .toString();
    }

}
