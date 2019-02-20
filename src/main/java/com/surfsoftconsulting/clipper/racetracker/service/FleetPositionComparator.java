package com.surfsoftconsulting.clipper.racetracker.service;

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

import com.surfsoftconsulting.clipper.racetracker.domain.Position;
import com.surfsoftconsulting.clipper.racetracker.domain.Race;
import com.surfsoftconsulting.clipper.racetracker.domain.Vessel;

import java.util.Comparator;
import java.util.Optional;

public class FleetPositionComparator implements Comparator<Vessel> {

    private final int raceNo;

    FleetPositionComparator(int raceNo) {
        this.raceNo = raceNo;
    }

    @Override
    public int compare(Vessel v1, Vessel v2) {

        Optional<Race> v1Race = v1.getRace(raceNo);
        Optional<Race> v2Race = v2.getRace(raceNo);

        if (v1Race.isPresent() && v2Race.isPresent()) {
            // Both vessels have race positions
            Position v1LatestPosition = v1Race.get().getLatestPosition();
            Position v2LatestPosition = v2Race.get().getLatestPosition();
            if (v1Race.get().isFinished()) {
                // fp1 finished
                if (v2Race.get().isFinished()) {
                    // Both finished, use position
                    return v1LatestPosition.getPosition() - v2LatestPosition.getPosition();
                }
                else {
                    // fp2 not finshed, fp1 is first
                    return -1;
                }
            }
            else if (!v1LatestPosition.isInStealthMode()) {
                // fp1 racing
                if (!v2LatestPosition.isInStealthMode()) {
                    // Both racing, use distance remaining
                    return v1LatestPosition.getDistanceRemaining().compareTo(v2LatestPosition.getDistanceRemaining());

                }
                else {
                    // fp2 in stealth mode, fp1 is first
                    return -1;
                }
            }
            else {
                // fp1 in stealth mode
                if (v2LatestPosition.isInStealthMode()) {
                    // Both in stealth mode, use timestamp
                    return v1LatestPosition.getTimestamp().compareTo(v2LatestPosition.getTimestamp());
                }
                else {
                    // fp2 not in stealth mode, fp2 is first
                    return 1;
                }

            }
        }
        else {
            return v1Race.isPresent() ? -1 : v2Race.isPresent() ? 1 : 0;
        }

    }

}