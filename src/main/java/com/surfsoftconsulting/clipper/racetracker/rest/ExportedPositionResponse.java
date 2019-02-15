package com.surfsoftconsulting.clipper.racetracker.rest;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

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
public class ExportedPositionResponse {

    private LocalDateTime timestamp;

    private int position;

    private Double latitude;

    private Double longitude;

    private Double speed;

    private Integer heading;

    private Double distanceRemaining;

    private Double distanceToLeadVessel;

    private Double distanceTravelled;

    private String status;

    ExportedPositionResponse(LocalDateTime timestamp,
                             int position,
                             Double latitude,
                             Double longitude,
                             Double speed,
                             Integer heading,
                             Double distanceRemaining,
                             Double distanceToLeadVessel,
                             Double distanceTravelled,
                             String status) {

        this.timestamp = timestamp;
        this.position = position;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.heading = heading;
        this.distanceRemaining = distanceRemaining;
        this.distanceToLeadVessel = distanceToLeadVessel;
        this.distanceTravelled = distanceTravelled;
        this.status = status;

    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getHeading() {
        return heading;
    }

    public void setHeading(Integer heading) {
        this.heading = heading;
    }

    public Double getDistanceRemaining() {
        return distanceRemaining;
    }

    public void setDistanceRemaining(Double distanceRemaining) {
        this.distanceRemaining = distanceRemaining;
    }

    public Double getDistanceToLeadVessel() {
        return distanceToLeadVessel;
    }

    public void setDistanceToLeadVessel(Double distanceToLeadVessel) {
        this.distanceToLeadVessel = distanceToLeadVessel;
    }

    public Double getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(Double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ExportedPositionResponse that = (ExportedPositionResponse) o;

        return new EqualsBuilder()
                .append(position, that.position)
                .append(timestamp, that.timestamp)
                .append(latitude, that.latitude)
                .append(longitude, that.longitude)
                .append(speed, that.speed)
                .append(heading, that.heading)
                .append(distanceRemaining, that.distanceRemaining)
                .append(distanceToLeadVessel, that.distanceToLeadVessel)
                .append(distanceTravelled, that.distanceTravelled)
                .append(status, that.status)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(timestamp)
                .append(position)
                .append(latitude)
                .append(longitude)
                .append(speed)
                .append(heading)
                .append(distanceRemaining)
                .append(distanceToLeadVessel)
                .append(distanceTravelled)
                .append(status)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("timestamp", timestamp)
                .append("position", position)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .append("speed", speed)
                .append("heading", heading)
                .append("distanceRemaining", distanceRemaining)
                .append("distanceToLeadVessel", distanceToLeadVessel)
                .append("distanceTravelled", distanceTravelled)
                .append("status", status)
                .toString();
    }

}
