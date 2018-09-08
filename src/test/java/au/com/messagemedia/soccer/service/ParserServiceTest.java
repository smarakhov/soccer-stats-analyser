package au.com.messagemedia.soccer.service;

import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ParserServiceTest {

  private ParserService parserService;

  @Before
  public void setuo() {
    parserService = new ParserService();
  }

  @Test
  public void testParse() throws IOException {
    parserService.parse(Resources.getResource("match-events-valid.csv").getFile());
  }
}