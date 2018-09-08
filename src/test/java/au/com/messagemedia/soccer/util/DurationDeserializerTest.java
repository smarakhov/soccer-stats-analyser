package au.com.messagemedia.soccer.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Test;

import java.io.IOException;
import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DurationDeserializerTest {

  private DurationDeserializer deserializer = new DurationDeserializer();

  @Test
  public void testDeserialize() throws IOException {
    // given
    DeserializationContext context = mock(DeserializationContext.class);
    JsonParser parser = mock(JsonParser.class);
    when(parser.getText()).thenReturn("72:34");

    // when
    Duration duration = deserializer.deserialize(parser, context);

    // then
    assertThat(duration, is(Duration.parse("PT1H12M34S")));
  }
}