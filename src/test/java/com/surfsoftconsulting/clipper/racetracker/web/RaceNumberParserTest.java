package com.surfsoftconsulting.clipper.racetracker.web;

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

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;

class RaceNumberParserTest {

    private static final String RACENO_CLASS = "raceno";

    private final RaceNumberParser underTest = new RaceNumberParser();

    private final Document standingsPage = mock(Document.class);
    private final Element body = mock(Element.class);
    private final Elements raceNoElements = mock(Elements.class);

    @BeforeEach
    void setUp() {
        when(standingsPage.body()).thenReturn(body);
        when(body.getElementsByClass(RACENO_CLASS)).thenReturn(raceNoElements);
    }

    @Test
    void wrongNumberOfElements() {

        when(raceNoElements.size()).thenReturn(2);

        assertThrows(IllegalStateException.class, () -> underTest.parse(standingsPage));

    }

    @Test
    void incorrectPrefix() {

        when(raceNoElements.size()).thenReturn(1);
        Element raceNoElement = mock(Element.class);
        when(raceNoElements.get(0)).thenReturn(raceNoElement);
        when(raceNoElement.text()).thenReturn("trash");

        assertThrows(IllegalStateException.class, () -> underTest.parse(standingsPage));

    }

    @Test
    void invalidNumber() {

        when(raceNoElements.size()).thenReturn(1);
        Element raceNoElement = mock(Element.class);
        when(raceNoElements.get(0)).thenReturn(raceNoElement);
        when(raceNoElement.text()).thenReturn("Race asd");

        assertThrows(IllegalStateException.class, () -> underTest.parse(standingsPage));

    }

    @Test
    void parse() {

        when(raceNoElements.size()).thenReturn(1);
        Element raceNoElement = mock(Element.class);
        when(raceNoElements.get(0)).thenReturn(raceNoElement);
        when(raceNoElement.text()).thenReturn("Race 2");

        assertThat(underTest.parse(standingsPage), is(2));

    }

}