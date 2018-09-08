package au.com.messagemedia.soccer.service;

import au.com.messagemedia.soccer.ex.ValidationException;
import au.com.messagemedia.soccer.model.MatchEvent;
import au.com.messagemedia.soccer.model.TeamStatistics;
import au.com.messagemedia.soccer.util.DurationDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AnalyserService {

  public Collection<TeamStatistics> analyse(List<MatchEvent> matchEvents, String timestamp) throws ValidationException {
    log.debug("analyse({}, {})", matchEvents, timestamp);

    // validate
    validateMatchEvents(matchEvents);
    Duration endTime = parseTimestamp(timestamp);

    // aggregate statistics while processing events
    StatisticsAggregator statisticsAggregator = new StatisticsAggregator();

    // filter by timestamp
    matchEvents.stream()
        .filter(matchEvent -> matchEvent.getTime().getSeconds() <= endTime.getSeconds())
        .forEach(matchEvent -> processEvent(matchEvent, statisticsAggregator));

    // update possession for remaining time
    statisticsAggregator.incrementCurrentTeamPossession(endTime);

    return statisticsAggregator.getStatistics().values();
  }

  void processEvent(MatchEvent matchEvent, StatisticsAggregator aggregator) {
    log.debug("processEvent({}, ...)", matchEvent);

    switch (matchEvent.getEventType()) {
      case START:
        aggregator.setCurrentEvent(matchEvent);
        break;
      case SHOT:
        aggregator.getTeamStatistics(matchEvent.getTeamName()).incrementShots();
        break;
      case SCORE:
        aggregator.getTeamStatistics(matchEvent.getTeamName()).incrementGoals();
        break;
      case POSSESS:
        aggregator.incrementCurrentTeamPossession(matchEvent.getTime());
        aggregator.setCurrentEvent(matchEvent);
        break;
      case BREAK:
      case END:
        aggregator.incrementCurrentTeamPossession(matchEvent.getTime());
        aggregator.setCurrentEvent(null);
        break;
    }
  }

  void validateMatchEvents(List<MatchEvent> matchEvents) throws ValidationException {
    for (MatchEvent matchEvent : matchEvents) {
      if (matchEvent.getEventType().isHasTeam() && StringUtils.isBlank(matchEvent.getTeamName())) {
        throw new ValidationException("Missing team name: " + matchEvent);
      }

      if (!matchEvent.getEventType().isHasTeam() && StringUtils.isNotBlank(matchEvent.getTeamName())) {
        throw new ValidationException("Unexpected team name: " + matchEvent);
      }
    }

    if (matchEvents.stream().map(MatchEvent::getTeamName).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList()).size() != 2) {
      throw new ValidationException("Expected 2 team names");
    }
  }

  Duration parseTimestamp(String timestamp) throws ValidationException {
    try {
      return DurationDeserializer.parse(timestamp);
    } catch (Exception e) {
      throw new ValidationException("Invalid timestamp: " + timestamp, e);
    }
  }
}
