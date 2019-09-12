package com.surfsoftconsulting.clipper.racetracker.web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RaceStandingsDataParserIntegrationTest {

    private static final String BASE_URI = "http://www.clipperroundtheworld.com";
    private static final String HTML_SOURCE = "/race-standings-19-20.html";

    private RaceStandingsDataFactory raceStandingsDataFactory = new RaceStandingsDataFactory(new TableRowParser());

    private final RaceStandingsDataParser underTest = new RaceStandingsDataParser(raceStandingsDataFactory);

    @Test
    void parse() throws Exception {

        Document document = Jsoup.parse(this.getClass().getResourceAsStream(HTML_SOURCE), Charset.defaultCharset().name(), BASE_URI);

        List<RaceStandingsData> raceStandingsData = underTest.parse(document);

        assertThat(raceStandingsData).hasSize(11);

    }

}
