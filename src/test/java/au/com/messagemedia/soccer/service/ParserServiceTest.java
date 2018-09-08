package au.com.messagemedia.soccer.service;

import au.com.messagemedia.soccer.model.MatchEvent;
import au.com.messagemedia.soccer.model.MatchEventType;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ParserServiceTest {

  private ParserService parserService;

  @Before
  public void setup() {
    parserService = new ParserService();
  }

  @Test
  public void testParse() throws IOException {
    // when
    List<MatchEvent> matchEvents = parserService.parse(Resources.getResource("match-events-valid.csv").getFile());

    // then
    assertThat(matchEvents.size(), is(9));

    // check the order
    assertThat(matchEvents.get(3), is(new MatchEvent(Duration.of(80, ChronoUnit.SECONDS), MatchEventType.SHOT, "A"))); // 01:20, SHOT, A
    assertThat(matchEvents.get(6), is(new MatchEvent(Duration.of(45, ChronoUnit.MINUTES), MatchEventType.BREAK, ""))); // 45:00, BREAK,
    assertThat(matchEvents.get(8), is(new MatchEvent(Duration.of(90, ChronoUnit.MINUTES), MatchEventType.END, null))); // 90:00, END
  }

  @Test(expected = IOException.class)
  public void testParseInvalidEventType() throws IOException {
    parserService.parse(Resources.getResource("match-events-invalid-event-type.csv").getFile());
  }

  @Test(expected = IOException.class)
  public void testParseInvalidTime() throws IOException {
    parserService.parse(Resources.getResource("match-events-invalid-time.csv").getFile());
  }
}