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
import org.springframework.data.annotation.Id;

import java.util.Comparator;
import java.util.Set;

import static java.util.Collections.emptySet;

public class Vessel {

    @Id
    private String id;

    private String name;

    private Set<Position> positions;

    public Vessel(String id, String name) {
        this.id = id;
        this.name = name;
        positions = emptySet();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Position> getPositions() {
        return positions == null ? emptySet() : positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }

    public Position getLatestPosition() {
        return getPositions().stream().sorted(Comparator.comparing(Position::getTimestamp).reversed()).findFirst().orElse(new Position());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Vessel vessel = (Vessel) o;

        return new EqualsBuilder()
                .append(id, vessel.id)
                .append(name, vessel.name)
                .append(positions, vessel.positions)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(positions)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("positions", positions)
                .toString();
    }

}