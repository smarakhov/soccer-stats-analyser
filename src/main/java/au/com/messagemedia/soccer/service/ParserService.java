package au.com.messagemedia.soccer.service;

import au.com.messagemedia.soccer.model.MatchEvent;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ParserService {

  public List<MatchEvent> parse(String filename) throws IOException {
    log.debug("parse({})", filename);

    CsvMapper mapper = new CsvMapper();
    mapper.enable(CsvParser.Feature.TRIM_SPACES);
    CsvSchema schema = mapper.schemaFor(MatchEvent.class).withHeader();
    MappingIterator<MatchEvent> it = mapper.readerFor(MatchEvent.class).with(schema).readValues(new File(filename));
    return it.readAll();
  }
}
