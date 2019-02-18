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

import com.surfsoftconsulting.clipper.racetracker.domain.Coordinates;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MapViewFactory {

    private static final String MAP_VIEW_URL_TEMPLATE = "https://www.google.com/maps/?q=%s,%s";

    String createMapView(Optional<Coordinates> coordinates) {
        return coordinates.map(c -> String.format(MAP_VIEW_URL_TEMPLATE, c.getLatitude(), c.getLongitude())).orElse(null);
    }

}
