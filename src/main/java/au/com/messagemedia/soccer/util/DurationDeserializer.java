package au.com.messagemedia.soccer.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;

public class DurationDeserializer extends JsonDeserializer<Duration> {
  @Override
  public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return parse(p.getText());
  }

  public static Duration parse(String timestamp) {
    String[] duration = timestamp.split(":");
    return Duration.parse("PT" + duration[0] + "M" + duration[1] + "S");
  }
}
