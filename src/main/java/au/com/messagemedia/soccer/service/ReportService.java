package au.com.messagemedia.soccer.service;

import au.com.messagemedia.soccer.model.TeamStatistics;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_SECOND;

@Slf4j
@AllArgsConstructor
public class ReportService {

  private static final String HEADER = "Timestamp, Team, Possession, Shot, Score";
  private static final double HUNDRED = 100D;
  private static final String PERCENTAGE_PATTERN = "#0'%'";
  private static final String TIMESTAMP_PATTERN = "mm:ss";

  private WriterService writerService;

  public void printReport(Collection<TeamStatistics> teamStatistics, String outputFilename) throws FileNotFoundException {
    log.debug("printReport({}, {})", teamStatistics, outputFilename);

    Duration totalTime = teamStatistics.stream()
        .map(TeamStatistics::getPossession)
        .reduce(Duration.ZERO, Duration::plus);

    try (PrintWriter writer = writerService.getWriter(outputFilename)) {
      writer.println(HEADER);

      teamStatistics.stream()
          .sorted(Comparator.comparing(TeamStatistics::getTeamName))
          .forEach(statistics -> writer.println(toReportLine(statistics, totalTime)));
    }
  }

  String toReportLine(TeamStatistics teamStatistics, Duration totalTime) {
    double percentage = totalTime.getSeconds() > 0
        ? HUNDRED * teamStatistics.getPossession().getSeconds() / totalTime.getSeconds()
        : 0;

    return Stream.of(
        DurationFormatUtils.formatDuration(totalTime.getSeconds() * MILLIS_PER_SECOND, TIMESTAMP_PATTERN, true),
        teamStatistics.getTeamName(),
        new DecimalFormat(PERCENTAGE_PATTERN).format(percentage),
        Integer.toString(teamStatistics.getShots()),
        Integer.toString(teamStatistics.getGoals()))
        .collect(Collectors.joining(", "));
  }
}
