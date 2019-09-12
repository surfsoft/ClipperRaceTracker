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

public class SpeedAndCourseData {

    private final String name;
    private final Double speed;
    private final Integer heading;

    public SpeedAndCourseData(String name, Double speed, Integer heading) {
        this.name = name;
        this.speed = speed;
        this.heading = heading;
    }

    public String getName() {
        return name;
    }

    public Double getSpeed() {
        return speed;
    }

    public Integer getHeading() {
        return heading;
    }

}
