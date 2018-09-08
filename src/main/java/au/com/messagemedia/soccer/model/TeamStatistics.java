package au.com.messagemedia.soccer.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class TeamStatistics {
  private String teamName;
  private Duration possession;
  private int shots;
  private int goals;

  public TeamStatistics(String teamName) {
    this.teamName = teamName;
    possession = Duration.ofSeconds(0);
    shots = 0;
    goals = 0;
  }

  public void incrementShots() {
    shots++;
  }

  public void incrementGoals() {
    goals++;
  }

  public void incrementPossession(long seconds) {
    this.possession = this.possession.plusSeconds(seconds);
  }
}
