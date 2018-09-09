package au.com.messagemedia.soccer.service;

import au.com.messagemedia.soccer.model.TeamStatistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {

  @Mock
  private WriterService writerService;

  @InjectMocks
  private ReportService reportService;

  @Test
  public void testPrintReport() throws FileNotFoundException {
    // given
    StringWriter stringWriter = new StringWriter();
    when(writerService.getWriter("output.csv")).thenReturn(new PrintWriter(stringWriter));

    Collection<TeamStatistics> teamStatistics = Arrays.asList(
        new TeamStatistics("B", Duration.ofSeconds(300), 5, 1),
        new TeamStatistics("A", Duration.ofSeconds(900), 10, 2)
    );

    // when
    reportService.printReport(teamStatistics, "output.csv");

    // then
    assertThat(stringWriter.toString(), is(
        "Timestamp, Team, Possession, Shot, Score\n" +
            "20:00, A, 75%, 10, 2\n" +
            "20:00, B, 25%, 5, 1\n"));
  }

  @Test(expected = FileNotFoundException.class)
  public void testPrintReportWithException() throws FileNotFoundException {
    // given
    when(writerService.getWriter("output.csv")).thenThrow(new FileNotFoundException("File not found"));

    // when
    reportService.printReport(Collections.emptyList(), "output.csv");
  }

  @Test
  public void testToReportLine() {
    // given
    TeamStatistics teamStatistics = new TeamStatistics("A", Duration.ofSeconds(30), 3, 1);

    // when
    String reportLine = reportService.toReportLine(teamStatistics, Duration.ofSeconds(300));

    // then
    assertThat(reportLine, is("05:00, A, 10%, 3, 1"));
  }

  @Test
  public void testToReportLineWhenTotalTimeIsZero() {
    // given
    TeamStatistics teamStatistics = new TeamStatistics("A", Duration.ZERO, 0, 0);

    // when
    String reportLine = reportService.toReportLine(teamStatistics, Duration.ZERO);

    // then
    assertThat(reportLine, is("00:00, A, 0%, 0, 0"));
  }
}
