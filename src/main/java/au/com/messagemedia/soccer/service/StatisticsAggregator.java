package au.com.messagemedia.soccer.service;

import au.com.messagemedia.soccer.model.MatchEvent;
import au.com.messagemedia.soccer.model.TeamStatistics;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Getter
class StatisticsAggregator {

  @Setter
  private MatchEvent currentEvent;

  private Map<String, TeamStatistics> statistics = new HashMap<>();

  TeamStatistics getTeamStatistics(String teamName) {
    return statistics.computeIfAbsent(teamName, TeamStatistics::new);
  }

  void incrementCurrentTeamPossession(Duration endTime) {
    if (currentEvent != null) {
      getTeamStatistics(currentEvent.getTeamName()).incrementPossession(endTime.minus(currentEvent.getTime()).getSeconds());
    }
  }
}
