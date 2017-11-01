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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;

public class Vessel {

    @Id
    private String id;

    private String name;

    private Set<Race> races;

    public Vessel(String id, String name) {
        this.id = id;
        this.name = name;
        races = new HashSet<>();
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

    public Set<Race> getRaces() {
        return races == null ? emptySet() : races;
    }

    public void setRaces(Set<Race> races) {
        this.races = races;
    }

    public Optional<Race> getRace(int raceNo) {
        return getRaces()
                .stream()
                .filter(r -> r.getRaceNo() == raceNo)
                .findFirst();

    }

    public Optional<Race> getLatestRace() {
        return getRaces().stream().sorted(Comparator.comparing(Race::getRaceNo).reversed()).findFirst();
    }

    public boolean hasNotStarted() {
        return races.isEmpty();
    }

    public String getStatus(int raceNo) {

        Optional<Race> race = getRace(raceNo);
        if (race.isPresent()) {
            if (race.get().isFinished()) {
                return "finished)";
            } else if (race.get().getLatestPosition().isInStealthMode()) {
                return "stealth mode";
            } else {
                return race.get().getLatestPosition().getStatus().toLowerCase();
            }
        }
        else {
            return "not available";
        }

    }

    public int getPosition(int raceNo) {
        return getRace(raceNo).map(r -> r.getLatestPosition().getPosition()).orElse(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Vessel vessel = (Vessel) o;

        return new EqualsBuilder()
                .append(id, vessel.id)
                .append(name, vessel.name)
                .append(races, vessel.races)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(races)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("races", races)
                .toString();
    }

}
