package au.com.messagemedia.soccer.service;

import au.com.messagemedia.soccer.model.MatchEvent;
import au.com.messagemedia.soccer.model.MatchEventType;
import au.com.messagemedia.soccer.model.TeamStatistics;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class StatisticsAggregatorTest {

  private StatisticsAggregator statisticsAggregator;

  @Before
  public void setup() {
    statisticsAggregator = new StatisticsAggregator();
  }

  @Test
  public void testGetTeamStatistics() {
    // team A
    TeamStatistics teamAStatistics = statisticsAggregator.getTeamStatistics("A");
    assertThat(teamAStatistics, notNullValue());

    // team B
    TeamStatistics teamBStatistics = statisticsAggregator.getTeamStatistics("B");
    assertThat(teamBStatistics, notNullValue());
    assertThat(teamBStatistics, not(teamAStatistics));

    // team A again
    assertThat(statisticsAggregator.getTeamStatistics("A"), is(teamAStatistics));
  }

  @Test
  public void testIncrementCurrentPossession() {
    // given
    statisticsAggregator.getTeamStatistics("A");
    statisticsAggregator.getTeamStatistics("B");
    statisticsAggregator.setCurrentEvent(new MatchEvent(Duration.ofSeconds(10), MatchEventType.START, "A"));

    // when
    statisticsAggregator.incrementCurrentTeamPossession(Duration.ofSeconds(25));

    // then
    assertThat(statisticsAggregator.getTeamStatistics("A").getPossession(), is(Duration.ofSeconds(15)));
    assertThat(statisticsAggregator.getTeamStatistics("B").getPossession(), is(Duration.ofSeconds(0)));
  }

  @Test
  public void testIncrementCurrentPossessionWhenNoCurrentEvent() {
    // given
    statisticsAggregator.getTeamStatistics("A");
    statisticsAggregator.getTeamStatistics("B");
    statisticsAggregator.setCurrentEvent(null);

    // when
    statisticsAggregator.incrementCurrentTeamPossession(Duration.ofSeconds(25));

    // then
    assertThat(statisticsAggregator.getTeamStatistics("A").getPossession(), is(Duration.ofSeconds(0)));
    assertThat(statisticsAggregator.getTeamStatistics("B").getPossession(), is(Duration.ofSeconds(0)));

  }
}