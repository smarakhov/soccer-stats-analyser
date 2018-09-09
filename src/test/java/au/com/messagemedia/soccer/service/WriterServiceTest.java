package au.com.messagemedia.soccer.service;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class WriterServiceTest {

  private WriterService writerService;

  @Before
  public void setup() {
    writerService = new WriterService();
  }

  @Test
  public void testGetWriterWithFilename() throws FileNotFoundException {
    // when
    PrintWriter writer = writerService.getWriter("output.cvs");

    // then
    assertThat(writer, notNullValue());
  }

  @Test(expected = FileNotFoundException.class)
  public void testGetWriterWithFilenameWithException() throws FileNotFoundException {
    writerService.getWriter(".");
  }

  @Test
  public void testGetWriterWithoutFilename() throws FileNotFoundException {
    // when
    PrintWriter writer = writerService.getWriter(null);

    // then
    assertThat(writer, notNullValue());
  }
}