package com.surfsoftconsulting.clipper.racetracker.web;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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

class RaceStandingsDataParserTest {

    private final RaceStandingsDataFactory raceStandingsDataFactory = mock(RaceStandingsDataFactory.class);

    private final RaceStandingsDataParser underTest = new RaceStandingsDataParser(raceStandingsDataFactory);

    @Test
    void parse() {

        Document raceStandingsPage = mock(Document.class);
        Element body = mock(Element.class);
        when(raceStandingsPage.body()).thenReturn(body);
        Element currentStandings = mock(Element.class);
        when(body.getElementById("currentstandings")).thenReturn(currentStandings);
        Elements standingsRows = mock(Elements.class);
        when(currentStandings.getElementsByTag("tr")).thenReturn(standingsRows);
        when(standingsRows.size()).thenReturn(4);
        Element row1 = mock(Element.class);
        Element row2 = mock(Element.class);
        Element row3 = mock(Element.class);
        when(standingsRows.get(1)).thenReturn(row1);
        when(standingsRows.get(2)).thenReturn(row2);
        when(standingsRows.get(3)).thenReturn(row3);
        RaceStandingsData raceStandingsData1 = mock(RaceStandingsData.class);
        RaceStandingsData raceStandingsData2 = mock(RaceStandingsData.class);
        RaceStandingsData raceStandingsData3 = mock(RaceStandingsData.class);
        when(raceStandingsDataFactory.fromTableRow(row1)).thenReturn(raceStandingsData1);
        when(raceStandingsDataFactory.fromTableRow(row2)).thenReturn(raceStandingsData2);
        when(raceStandingsDataFactory.fromTableRow(row3)).thenReturn(raceStandingsData3);

        assertThat(underTest.parse(raceStandingsPage), is(asList(raceStandingsData1, raceStandingsData2, raceStandingsData3)));


    }

}