package au.com.messagemedia.soccer.model;

import au.com.messagemedia.soccer.util.MinutesSecondsDeserializer;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

import java.time.Duration;

@Value
@JsonPropertyOrder({"time", "eventType", "team"})
public class MatchEvent {
  @JsonDeserialize(using = MinutesSecondsDeserializer.class)
  private Duration time;
  private MatchEventType eventType;
  private String team;
}
