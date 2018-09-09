package au.com.messagemedia.soccer.service;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class WriterServiceTest {

  private static final String TEST_OUTPUT_FILENAME = "test-output.csv";

  private WriterService writerService;

  @AfterClass
  public static void afterClass() {
    new File(TEST_OUTPUT_FILENAME).deleteOnExit();
  }

  @Before
  public void setup() {
    writerService = new WriterService();
  }

  @Test
  public void testGetWriterWithFilename() throws FileNotFoundException {
    // when
    PrintWriter writer = writerService.getWriter(TEST_OUTPUT_FILENAME);

    // then
    assertThat(writer, notNullValue());
    assertThat(new File(TEST_OUTPUT_FILENAME).exists(), is(true));
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