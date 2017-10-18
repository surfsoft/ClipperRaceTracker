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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Race {

    private int raceNo;

    private Set<Position> positions;

    private LocalDateTime finishTime;

    public Race() {    }

    public Race(int raceNo) {
        this.raceNo = raceNo;
        positions = new HashSet<>();
    }

    public int getRaceNo() {
        return raceNo;
    }

    public void setRaceNo(int raceNo) {
        this.raceNo = raceNo;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    @JsonIgnore
    public Position getLatestPosition() {
        return positions
                .stream()
                .sorted(Comparator.comparing(Position::getTimestamp).reversed())
                .findFirst()
                .orElse(new Position());
    }

    @JsonIgnore
    public void addPosition(Position position) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Race race = (Race) o;

        return new EqualsBuilder()
                .append(raceNo, race.raceNo)
                .append(positions, race.positions)
                .append(finishTime, race.finishTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(raceNo)
                .append(positions)
                .append(finishTime)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("raceNo", raceNo)
                .append("positions", positions)
                .append("finishTime", finishTime)
                .toString();
    }

}
