package com.surfsoftconsulting.clipper.racetracker.domain;

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

public class Position {

    private int position;

    private Coordinates coordinates;

    private Double speed;

    private Integer heading;

    private Double distanceRemaining;

    private Double distanceToLeadVessel;

    private Double distanceTravelled;

    private LocalDateTime timestamp;

    private String status;

    private LocalDateTime finishTimestamp;

    private boolean inStealthMode;

    public Position() {
    }

    Position(int position, Coordinates coordinates, Double speed, Integer heading, Double distanceRemaining, Double distanceToLeadVessel, Double distanceTravelled, LocalDateTime timestamp, String status, LocalDateTime finishTimestamp, boolean inStealthMode) {
        this.position = position;
        this.coordinates = coordinates;
        this.speed = speed;
        this.heading = heading;
        this.distanceRemaining = distanceRemaining;
        this.distanceToLeadVessel = distanceToLeadVessel;
        this.distanceTravelled = distanceTravelled;
        this.timestamp = timestamp;
        this.status = status;
        this.finishTimestamp = finishTimestamp;
        this.inStealthMode = inStealthMode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getFinishTimestamp() {
        return finishTimestamp;
    }

    public void setFinishTimestamp(LocalDateTime finishTimestamp) {
        this.finishTimestamp = finishTimestamp;
    }

    public boolean isInStealthMode() {
        return inStealthMode;
    }

    public void setInStealthMode(boolean inStealthMode) {
        this.inStealthMode = inStealthMode;
    }

    public boolean isFinished() {
        return finishTimestamp != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Position position1 = (Position) o;

        return new EqualsBuilder()
                .append(position, position1.position)
                .append(inStealthMode, position1.inStealthMode)
                .append(coordinates, position1.coordinates)
                .append(speed, position1.speed)
                .append(heading, position1.heading)
                .append(distanceRemaining, position1.distanceRemaining)
                .append(distanceToLeadVessel, position1.distanceToLeadVessel)
                .append(distanceTravelled, position1.distanceTravelled)
                .append(timestamp, position1.timestamp)
                .append(status, position1.status)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(position)
                .append(coordinates)
                .append(speed)
                .append(heading)
                .append(distanceRemaining)
                .append(distanceToLeadVessel)
                .append(distanceTravelled)
                .append(timestamp)
                .append(status)
                .append(inStealthMode)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("position", position)
                .append("coordinates", coordinates)
                .append("speed", speed)
                .append("heading", heading)
                .append("distanceRemaining", distanceRemaining)
                .append("distanceToLeadVessel", distanceToLeadVessel)
                .append("distanceTravelled", distanceTravelled)
                .append("timestamp", timestamp)
                .append("status", status)
                .append("inStealthMode", inStealthMode)
                .toString();
    }

}
