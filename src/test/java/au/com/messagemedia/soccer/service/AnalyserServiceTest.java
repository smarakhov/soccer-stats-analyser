package au.com.messagemedia.soccer.service;

import au.com.messagemedia.soccer.ex.ValidationException;
import au.com.messagemedia.soccer.model.MatchEvent;
import au.com.messagemedia.soccer.model.MatchEventType;
import au.com.messagemedia.soccer.model.TeamStatistics;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnalyserServiceTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private AnalyserService analyserService;

  @Before
  public void setup() {
    analyserService = new AnalyserService();
  }

  @Test
  public void testAnalyse() throws ValidationException {
    // given
    List<MatchEvent> matchEvents = Arrays.asList(
        new MatchEvent(Duration.ofSeconds(0), MatchEventType.START, "A"),
        new MatchEvent(Duration.ofSeconds(10), MatchEventType.POSSESS, "B"),
        new MatchEvent(Duration.ofSeconds(20), MatchEventType.SHOT, "B"),
        new MatchEvent(Duration.ofSeconds(20), MatchEventType.SCORE, "B"),
        new MatchEvent(Duration.ofSeconds(20), MatchEventType.POSSESS, "A"),
        new MatchEvent(Duration.ofSeconds(30), MatchEventType.POSSESS, "B"),
        new MatchEvent(Duration.ofSeconds(40), MatchEventType.POSSESS, "A")
    );

    // when
    Collection<TeamStatistics> statistics = analyserService.analyse(matchEvents, "00:35");

    // then
    assertThat(statistics.size(), is(2));

    Optional<TeamStatistics> teamAStatistics =
        statistics.stream().filter(teamStatistics -> teamStatistics.getTeamName().equals("A")).findFirst();
    Optional<TeamStatistics> teamBStatistics =
        statistics.stream().filter(teamStatistics -> teamStatistics.getTeamName().equals("B")).findFirst();

    assertThat(teamAStatistics.isPresent(), is(true));
    assertThat(teamAStatistics.get(), is(new TeamStatistics("A", Duration.ofSeconds(20), 0, 0)));

    assertThat(teamBStatistics.isPresent(), is(true));
    assertThat(teamBStatistics.get(), is(new TeamStatistics("B", Duration.ofSeconds(15), 1, 1)));
  }

  @Test
  public void testProcessStartEvent() {
    // given
    MatchEvent event = new MatchEvent(Duration.ofSeconds(0), MatchEventType.START, "A");
    StatisticsAggregator aggregator = mock(StatisticsAggregator.class);

    // when
    analyserService.processEvent(event, aggregator);

    // then
    verify(aggregator).setCurrentEvent(event);
  }

  @Test
  public void testProcessShotEvent() {
    // given
    MatchEvent event = new MatchEvent(Duration.ofSeconds(30), MatchEventType.SHOT, "A");

    StatisticsAggregator aggregator = mock(StatisticsAggregator.class);
    TeamStatistics teamStatistics = mock(TeamStatistics.class);
    when(aggregator.getTeamStatistics("A")).thenReturn(teamStatistics);

    // when
    analyserService.processEvent(event, aggregator);

    // then
    verify(teamStatistics).incrementShots();
  }

  @Test
  public void testProcessScoreEvent() {
    // given
    MatchEvent event = new MatchEvent(Duration.ofSeconds(30), MatchEventType.SCORE, "A");

    StatisticsAggregator aggregator = mock(StatisticsAggregator.class);
    TeamStatistics teamStatistics = mock(TeamStatistics.class);
    when(aggregator.getTeamStatistics("A")).thenReturn(teamStatistics);

    // when
    analyserService.processEvent(event, aggregator);

    // then
    verify(teamStatistics).incrementGoals();
  }

  @Test
  public void testProcessPossessEvent() {
    // given
    MatchEvent event = new MatchEvent(Duration.ofSeconds(30), MatchEventType.POSSESS, "A");
    StatisticsAggregator aggregator = mock(StatisticsAggregator.class);

    // when
    analyserService.processEvent(event, aggregator);

    // then
    verify(aggregator).incrementCurrentTeamPossession(Duration.ofSeconds(30));
    verify(aggregator).setCurrentEvent(event);
  }

  @Test
  public void testProcessBreakEvent() {
    // given
    MatchEvent event = new MatchEvent(Duration.ofMinutes(45), MatchEventType.BREAK, null);
    StatisticsAggregator aggregator = mock(StatisticsAggregator.class);

    // when
    analyserService.processEvent(event, aggregator);

    // then
    verify(aggregator).incrementCurrentTeamPossession(Duration.ofMinutes(45));
    verify(aggregator).setCurrentEvent(null);
  }

  @Test
  public void testProcessEndEvent() {
    // given
    MatchEvent event = new MatchEvent(Duration.ofMinutes(90), MatchEventType.END, null);
    StatisticsAggregator aggregator = mock(StatisticsAggregator.class);

    // when
    analyserService.processEvent(event, aggregator);

    // then
    verify(aggregator).incrementCurrentTeamPossession(Duration.ofMinutes(90));
    verify(aggregator).setCurrentEvent(null);
  }

  @Test
  public void testValidateMatchEvents() throws ValidationException {
    // given
    List<MatchEvent> matchEvents = ImmutableList.of(
        new MatchEvent(Duration.ofSeconds(0), MatchEventType.START, "A"),
        new MatchEvent(Duration.ofMinutes(1), MatchEventType.POSSESS, "B"),
        new MatchEvent(Duration.ofMinutes(45), MatchEventType.BREAK, null),
        new MatchEvent(Duration.ofMinutes(90), MatchEventType.END, "")
    );

    // when
    analyserService.validateMatchEvents(matchEvents);
  }

  @Test
  public void testValidateMatchEventsWhenMissingTeamName() throws ValidationException {
    // given
    List<MatchEvent> matchEvents = ImmutableList.of(
        new MatchEvent(Duration.ofSeconds(0), MatchEventType.START, null)
    );

    expectedException.expect(ValidationException.class);
    expectedException.expectMessage("Missing team name: MatchEvent(time=PT0S, eventType=START, teamName=null)");

    // when
    analyserService.validateMatchEvents(matchEvents);
  }

  @Test
  public void testValidateMatchEventsWhenUnexpectedTeamName() throws ValidationException {
    // given
    List<MatchEvent> matchEvents = ImmutableList.of(
        new MatchEvent(Duration.ofMinutes(45), MatchEventType.BREAK, "A")
    );

    expectedException.expect(ValidationException.class);
    expectedException.expectMessage("Unexpected team name: MatchEvent(time=PT45M, eventType=BREAK, teamName=A)");

    // when
    analyserService.validateMatchEvents(matchEvents);
  }

  @Test
  public void testValidateMatchEventsWhenTooManyTeamNames() throws ValidationException {
    // given
    List<MatchEvent> matchEvents = ImmutableList.of(
        new MatchEvent(Duration.ofSeconds(0), MatchEventType.START, "A"),
        new MatchEvent(Duration.ofMinutes(1), MatchEventType.POSSESS, "B"),
        new MatchEvent(Duration.ofMinutes(2), MatchEventType.POSSESS, "C")
    );

    expectedException.expect(ValidationException.class);
    expectedException.expectMessage("Expected 2 team names");

    // when
    analyserService.validateMatchEvents(matchEvents);
  }

  @Test
  public void testParseTimestamp() throws ValidationException {
    // when
    Duration timestamp = analyserService.parseTimestamp("71:37");

    // then
    assertThat(timestamp, is(Duration.ofMinutes(71).plusSeconds(37)));
  }

  @Test
  public void testParseTimestampWhenInvalidTime() throws ValidationException {
    expectedException.expect(ValidationException.class);
    expectedException.expectMessage("Invalid timestamp: 7137");

    // when
    analyserService.parseTimestamp("7137");
  }
}