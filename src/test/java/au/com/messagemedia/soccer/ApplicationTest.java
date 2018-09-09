package au.com.messagemedia.soccer;

import au.com.messagemedia.soccer.ex.ValidationException;
import au.com.messagemedia.soccer.model.MatchEvent;
import au.com.messagemedia.soccer.model.MatchEventType;
import au.com.messagemedia.soccer.model.TeamStatistics;
import au.com.messagemedia.soccer.service.AnalyserService;
import au.com.messagemedia.soccer.service.ParserService;
import au.com.messagemedia.soccer.service.ReportService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {

  @Rule
  public final ExpectedSystemExit exit = ExpectedSystemExit.none();

  @Mock
  private AnalyserService analyserService;

  @Mock
  private ParserService parserService;

  @Mock
  private ReportService reportService;

  @InjectMocks
  private Application application;

  @Test
  public void testRunAnalyse() throws IOException, ValidationException {
    // given
    List<MatchEvent> matchEvents = Arrays.asList(
        new MatchEvent(Duration.ZERO, MatchEventType.START, "A"),
        new MatchEvent(Duration.ofSeconds(10), MatchEventType.POSSESS, "B")
    );
    when(parserService.parse("input.csv")).thenReturn(matchEvents);

    Collection<TeamStatistics> teamStatistics = Arrays.asList(
        new TeamStatistics("A", Duration.ofSeconds(10), 0, 0),
        new TeamStatistics("B", Duration.ofSeconds(1), 0, 0)
    );
    when(analyserService.analyse(matchEvents, "00:11")).thenReturn(teamStatistics);

    // when
    application.runAnalyser("input.csv", "00:11", "output.csv");

    // then
    verify(reportService).printReport(teamStatistics, "output.csv");
  }

  @Test
  public void testMainWith2Arguments() throws IOException, ValidationException {
    // given
    Application application = mock(Application.class);
    Application.setApplicationSupplier(() -> application);

    // when
    Application.main(new String[]{"file.csv", "00:11", null});

    // then
    verify(application).runAnalyser("file.csv", "00:11", null);
  }

  @Test
  public void testMainWith3Arguments() throws IOException, ValidationException {
    // given
    Application application = mock(Application.class);
    Application.setApplicationSupplier(() -> application);

    // when
    Application.main(new String[]{"input.csv", "00:11", "output.csv"});

    // then
    verify(application).runAnalyser("input.csv", "00:11", "output.csv");
  }

  @Test
  public void testMainWhenApplicationThrowsIoException() throws IOException, ValidationException {
    exit.expectSystemExitWithStatus(1);

    // given
    Application application = mock(Application.class);
    Application.setApplicationSupplier(() -> application);
    doThrow(new IOException("Failed to parse the file")).when(application).runAnalyser("file.csv", "00:11", null);

    // when
    Application.main(new String[]{"file.csv", "00:11", null});
  }

  @Test
  public void testMainWhenApplicationThrowsValidationException() throws IOException, ValidationException {
    exit.expectSystemExitWithStatus(1);

    // given
    Application application = mock(Application.class);
    Application.setApplicationSupplier(() -> application);

    doThrow(new ValidationException("Validation error")).when(application).runAnalyser("file.csv", "00:11", null);

    // when
    Application.main(new String[]{"file.csv", "00:11", null});
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