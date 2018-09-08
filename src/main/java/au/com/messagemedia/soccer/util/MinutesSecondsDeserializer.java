package au.com.messagemedia.soccer.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;

public class MinutesSecondsDeserializer extends JsonDeserializer<Duration> {
  @Override
  public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String[] duration = p.getText().split(":");
    return Duration.parse("PT" + duration[0] + "M" + duration[1] + "S");
  }
}
