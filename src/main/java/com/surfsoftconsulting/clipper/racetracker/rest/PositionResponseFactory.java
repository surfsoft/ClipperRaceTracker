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

import com.surfsoftconsulting.clipper.racetracker.domain.Position;
import org.springframework.stereotype.Component;

@Component
public class PositionResponseFactory {

    private final CoordinatesResponseFactory coordinatesResponseFactory;
    private final MapViewFactory mapViewFactory;

    public PositionResponseFactory(CoordinatesResponseFactory coordinatesResponseFactory, MapViewFactory mapViewFactory) {
        this.coordinatesResponseFactory = coordinatesResponseFactory;
        this.mapViewFactory = mapViewFactory;
    }

    public PositionResponse toPositionResponse(String id, String name, Position position) {
        CoordinatesResponse coordinatesResponse = coordinatesResponseFactory.getCoordinatesResponse(position.getCoordinates());
        String mapView = mapViewFactory.createMapView(position.getCoordinates());
        return new PositionResponse(id, name, position.getPosition(), position.isInStealthMode() ? "s" : "r", coordinatesResponse, mapView);
    }

}
