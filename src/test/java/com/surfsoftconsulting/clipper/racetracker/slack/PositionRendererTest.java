package com.surfsoftconsulting.clipper.racetracker.slack;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/*
 * Copyright 2019 Phil Haigh
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
class PositionRendererTest {

    @ParameterizedTest(name = "\"{0}\" should be \"{1}\"")
    @MethodSource("testData")
    void toPosition(int position, String expected) {
        assertThat(new PositionRenderer().toPosition(position), is(expected));
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(1, "1st"),
                Arguments.of(2, "2nd"),
                Arguments.of(3, "3rd"),
                Arguments.of(4, "4th"),
                Arguments.of(5, "5th"),
                Arguments.of(6, "6th"),
                Arguments.of(7, "7th"),
                Arguments.of(8, "8th"),
                Arguments.of(9, "9th"),
                Arguments.of(10, "10th"),
                Arguments.of(11, "11th"),
                Arguments.of(12, "12th")
        );
    }

}