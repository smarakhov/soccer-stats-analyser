package au.com.messagemedia.soccer;

import au.com.messagemedia.soccer.service.ParserService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {

  @Rule
  public final ExpectedSystemExit exit = ExpectedSystemExit.none();

  @Mock
  private ParserService parserService;

  @InjectMocks
  private Application application;

  @Test
  public void testRunAnalyse() throws IOException {
    // given

    // when
    application.runAnalyser("file.csv", "00:11", null);

    // then
    verify(parserService).parse("file.csv");
  }

  @Test
  public void testMainWith2Arguments() throws IOException {
    // given
    Application application = mock(Application.class);
    Application.setApplicationSupplier(() -> application);

    // when
    Application.main(new String[]{"file.csv", "00:11", null});

    // then
    verify(application).runAnalyser("file.csv", "00:11", null);
  }

  @Test
  public void testMainWith2ArgumentsWhenApplicationThrowsException() throws IOException {
    exit.expectSystemExitWithStatus(1);

    // given
    Application application = mock(Application.class);
    Application.setApplicationSupplier(() -> application);
    doThrow(new IOException("Failed to parse the file")).when(application).runAnalyser("file.csv", "00:11", null);

    // when
    Application.main(new String[]{"file.csv", "00:11", null});
  }

  @Test
  public void testMainWith3Arguments() throws IOException {
    // given
    Application application = mock(Application.class);
    Application.setApplicationSupplier(() -> application);

    // when
    Application.main(new String[]{"input.csv", "00:11", "output.csv"});

    // then
    verify(application).runAnalyser("input.csv", "00:11", "output.csv");
  }

  @Test
  public void testMainWith1Argument() {
    exit.expectSystemExitWithStatus(1);

    // when
    Application.main(new String[]{"input.csv"});
  }

  @Test
  public void testMainWith4Argument() {
    exit.expectSystemExitWithStatus(1);

    // when
    Application.main(new String[]{"input.csv", "00:11", "output.csv", "4"});
  }
}