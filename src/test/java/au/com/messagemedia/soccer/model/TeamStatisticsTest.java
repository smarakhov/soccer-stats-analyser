package au.com.messagemedia.soccer.model;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TeamStatisticsTest {

  private TeamStatistics teamStatistics;

  @Before
  public void setup() {
    teamStatistics = new TeamStatistics("A");
  }

  @Test
  public void testConstructor() {
    assertThat(teamStatistics.getTeamName(), is("A"));
    assertThat(teamStatistics.getGoals(), is(0));
    assertThat(teamStatistics.getShots(), is(0));
    assertThat(teamStatistics.getPossession(), is(Duration.ZERO));
  }

  @Test
  public void testIncrementGoals() {
    // when
    teamStatistics.incrementGoals();

    // then
    assertThat(teamStatistics.getGoals(), is(1));
  }

  @Test
  public void testIncrementShots() {
    // when
    teamStatistics.incrementShots();

    // then
    assertThat(teamStatistics.getShots(), is(1));
  }

  @Test
  public void testIncrementPossession() {
    // when
    teamStatistics.incrementPossession(42);
    teamStatistics.incrementPossession(22);

    // then
    assertThat(teamStatistics.getPossession(), is(Duration.ofSeconds(64)));
  }
}