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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpeedAndCourseDataResolverTest {

    private static final String VESSEL_1_NAME = "Boaty McBoatface 1";
    private static final String VESSEL_2_NAME = "Boaty McBoatface 2";
    private static final String VESSEL_3_NAME = "Boaty McBoatface 3";
    private static final String VESSEL_4_NAME = "Boaty McBoatface 4";

    private final SpeedAndCourseDataResolver underTest = new SpeedAndCourseDataResolver();

    private final SpeedAndCourseData vessel1 = mock(SpeedAndCourseData.class);
    private final SpeedAndCourseData vessel2 = mock(SpeedAndCourseData.class);
    private final SpeedAndCourseData vessel3 = mock(SpeedAndCourseData.class);

    private final List<SpeedAndCourseData> speedAndCourseData = asList(vessel1, vessel2, vessel3);

    @BeforeEach
    void setUp() {
        when(vessel1.getName()).thenReturn(VESSEL_1_NAME);
        when(vessel2.getName()).thenReturn(VESSEL_2_NAME);
        when(vessel3.getName()).thenReturn(VESSEL_3_NAME);
    }

    @Test
    void resolveWithMatch() {
        assertThat(underTest.resolve(VESSEL_2_NAME, speedAndCourseData), is(vessel2));
    }

    @Test
    void resolveNoMatch() {
        assertThat(underTest.resolve(VESSEL_4_NAME, speedAndCourseData), is(nullValue()));
    }


}