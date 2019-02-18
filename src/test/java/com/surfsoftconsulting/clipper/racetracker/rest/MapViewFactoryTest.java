package com.surfsoftconsulting.clipper.racetracker.rest;

import com.surfsoftconsulting.clipper.racetracker.domain.Coordinates;
import org.junit.jupiter.api.Test;

import static java.util.Optional.ofNullable;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
class MapViewFactoryTest {

    private static final Double LATITUDE = 21.456;
    private static final Double LONGITUDE = 14.234;
    private static final String MAP_VIEW_URL_TEMPLATE = "https://www.google.com/maps/?q=%s,%s";

    private final MapViewFactory underTest = new MapViewFactory();

    @Test
    void coordinatesPresent() {

        Coordinates coordinates = mock(Coordinates.class);

        when(coordinates.getLatitude()).thenReturn(LATITUDE);
        when(coordinates.getLongitude()).thenReturn(LONGITUDE);

        assertThat(underTest.createMapView(ofNullable(coordinates)), is(String.format(MAP_VIEW_URL_TEMPLATE, LATITUDE, LONGITUDE)));

    }

    @Test
    void noCoordinatesPresent() {
        assertThat(underTest.createMapView(ofNullable(null)), is(nullValue()));
    }

}