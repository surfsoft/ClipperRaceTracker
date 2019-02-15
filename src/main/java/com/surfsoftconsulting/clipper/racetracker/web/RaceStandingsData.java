package com.surfsoftconsulting.clipper.racetracker.web;

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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

public class RaceStandingsData {

    private final Integer position;
    private final String name;
    private final Double latitude;
    private final Double longitude;
    private final Double distanceRemaining;
    private final Double distanceToLeadVessel;
    private final Double distanceTravelled;
    private final LocalDateTime timestamp;
    private final String status;
    private final LocalDateTime finishTime;
    private final boolean stealthMode;

    public RaceStandingsData(Integer position, String name, Double latitude, Double longitude, Double distanceRemaining, Double distanceToLeadVessel, Double distanceTravelled, LocalDateTime timestamp, String status, LocalDateTime finishTime, boolean stealthMode) {
        this.position = position;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceRemaining = distanceRemaining;
        this.distanceToLeadVessel = distanceToLeadVessel;
        this.distanceTravelled = distanceTravelled;
        this.timestamp = timestamp;
        this.status = status;
        this.finishTime = finishTime;
        this.stealthMode = stealthMode;
    }

    public Integer getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getDistanceRemaining() {
        return distanceRemaining;
    }

    public Double getDistanceToLeadVessel() {
        return distanceToLeadVessel;
    }

    public Double getDistanceTravelled() {
        return distanceTravelled;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public boolean isInStealthMode() {
        return stealthMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RaceStandingsData that = (RaceStandingsData) o;

        return new EqualsBuilder()
                .append(stealthMode, that.stealthMode)
                .append(position, that.position)
                .append(name, that.name)
                .append(latitude, that.latitude)
                .append(longitude, that.longitude)
                .append(distanceRemaining, that.distanceRemaining)
                .append(distanceToLeadVessel, that.distanceToLeadVessel)
                .append(distanceTravelled, that.distanceTravelled)
                .append(timestamp, that.timestamp)
                .append(status, that.status)
                .append(finishTime, that.finishTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(position)
                .append(name)
                .append(latitude)
                .append(longitude)
                .append(distanceRemaining)
                .append(distanceToLeadVessel)
                .append(distanceTravelled)
                .append(timestamp)
                .append(status)
                .append(finishTime)
                .append(stealthMode)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("position", position)
                .append("name", name)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .append("distanceRemaining", distanceRemaining)
                .append("distanceToLeadVessel", distanceToLeadVessel)
                .append("distanceTravelled", distanceTravelled)
                .append("timestamp", timestamp)
                .append("status", status)
                .append("finishTime", finishTime)
                .append("stealthMode", stealthMode)
                .toString();
    }

}
