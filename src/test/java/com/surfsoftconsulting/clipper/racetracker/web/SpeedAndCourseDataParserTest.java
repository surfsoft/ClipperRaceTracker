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

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpeedAndCourseDataParserTest {

    private final String SCRIPT =
            "content: \"" +
            "<div class=\"markerInfo\" style=\"position: absolute; visibility: visible; width: 170px; left: 986.403px; bottom: -366.633px; cursor: default;\">" +
            "    <h3 class=\"\" style=\"background-color: #166fb8; color: #FFFFFF\"><span>Unicef</span></h3>" +
            "    <div><span>SOG</span><span class=\"lc\">11.3kn</span></div>" +
            "    <div><span>COG</span><span>112</span></div>" +
            "    <div><span>DTF</span><span class=\"lc\">2788.67nm</span></div>" +
            "    <div><span>Updated</span><span>42 mins ago</span>" +
            "</div><div><img src=\"/inc/img/logo35_small.png\"></div></div>\"\n" +
            "// some random JavaScript here" +
            "content: \"" +
            "<div class=\"markerInfo\" style=\"position: absolute; visibility: visible; width: 170px; left: 986.403px; bottom: -366.633px; cursor: default;\">" +
            "    <h3 class=\"\" style=\"background-color: #166fb8; color: #FFFFFF\"><span>Visit Seattle</span></h3>" +
            "    <div><span>SOG</span><span class=\"lc\">11.4kn</span></div>" +
            "    <div><span>COG</span><span>113</span></div>" +
            "    <div><span>DTF</span><span class=\"lc\">2888.67nm</span></div>" +
            "    <div><span>Updated</span><span>43 mins ago</span>" +
            "</div><div><img src=\"/inc/img/logo35_small.png\"></div></div>\"\n" +
            "content: \"" +
            "<div class=\"markerInfo\" style=\"position: absolute; visibility: visible; width: 170px; left: 986.403px; bottom: -366.633px; cursor: default;\">" +
            "    <h3 class=\"\" style=\"background-color: #166fb8; color: #FFFFFF\"><span>GREAT Britain</span></h3>" +
            "    <div><span>SOG</span><span class=\"lc\">11.5kn</span></div>" +
            "    <div><span>COG</span><span>114</span></div>" +
            "    <div><span>DTF</span><span class=\"lc\">2988.67nm</span></div>" +
            "    <div><span>Updated</span><span>44 mins ago</span>" +
            "</div><div><img src=\"/inc/img/logo35_small.png\"></div></div>\"\n";


    private final SpeedAndCourseDataParser underTest = new SpeedAndCourseDataParser();

    @Test
    void parse() {

        Document raceStandingsPage = mock(Document.class);
        Element body = mock(Element.class);
        when(raceStandingsPage.body()).thenReturn(body);
        Elements scripts = mock(Elements.class);
        when(body.getElementsByTag("script")).thenReturn(scripts);
        when(scripts.size()).thenReturn(2);
        Element script1 = mockScriptElement("");
        Element script2 = mockScriptElement(SCRIPT);
        when(scripts.get(0)).thenReturn(script1);
        when(scripts.get(1)).thenReturn(script2);

        List<SpeedAndCourseData> speedAndCourseData = underTest.parse(raceStandingsPage);
        assertThat(speedAndCourseData, hasSize(3));
        assertThat(speedAndCourseData.get(0).getName(), is("Unicef"));
        assertThat(speedAndCourseData.get(1).getName(), is("Visit Seattle"));
        assertThat(speedAndCourseData.get(2).getName(), is("GREAT Britain"));

    }

    @Test
    void notFound() {

        Document raceStandingsPage = mock(Document.class);
        Element body = mock(Element.class);
        when(raceStandingsPage.body()).thenReturn(body);
        Elements scripts = mock(Elements.class);
        when(body.getElementsByTag("script")).thenReturn(scripts);
        when(scripts.size()).thenReturn(2);
        Element script1 = mockScriptElement("");
        Element script2 = mockScriptElement("");
        when(scripts.get(0)).thenReturn(script1);
        when(scripts.get(1)).thenReturn(script2);

        assertThat(underTest.parse(raceStandingsPage), is(emptyList()));

    }

    private Element mockScriptElement(String wholeData) {

        Element scriptElement = mock(Element.class);

        DataNode dataNode = mock(DataNode.class);
        when(dataNode.getWholeData()).thenReturn(wholeData);
        when(scriptElement.dataNodes()).thenReturn(singletonList(dataNode));

        return scriptElement;

    }

}