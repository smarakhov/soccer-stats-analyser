package au.com.messagemedia.soccer;

import au.com.messagemedia.soccer.ex.ValidationException;
import au.com.messagemedia.soccer.model.MatchEvent;
import au.com.messagemedia.soccer.model.TeamStatistics;
import au.com.messagemedia.soccer.service.AnalyserService;
import au.com.messagemedia.soccer.service.ParserService;
import au.com.messagemedia.soccer.service.ReportService;
import au.com.messagemedia.soccer.service.WriterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
public class Application {

  private static final String TITLE = "Soccer Match Stats Analyser";

  /**
   * By default, application supplier creates new Application instance and all required services. Unit tests can
   * replace it with mocks if required
   */
  private static Supplier<Application> applicationSupplier =
      () -> new Application(new ParserService(), new AnalyserService(), new ReportService(new WriterService()));

  private ParserService parserService;
  private AnalyserService analyserService;
  private ReportService reportService;

  void runAnalyser(String inputFilename, String timestamp, String outputFilename) throws IOException, ValidationException {
    log.debug("runAnalyser({}, {}, {})", inputFilename, timestamp, outputFilename);

    List<MatchEvent> matchEvents = parserService.parse(inputFilename);
    Collection<TeamStatistics> teamStatistics = analyserService.analyse(matchEvents, timestamp);
    reportService.printReport(teamStatistics, outputFilename);
  }

  public static void main(String[] args) {
    log.info("{} started", TITLE);

    if (args.length < 2 || args.length > 3) {
      printUsage();
      System.exit(1);
    }

    try {
      applicationSupplier.get().runAnalyser(args[0], args[1], args.length > 2 ? args[2] : null);
    } catch (IOException|ValidationException exception) {
      log.error("Error while running analyser: {}", exception.getMessage(), exception);
      System.exit(1);
    }
  }

  private static void printUsage() {
    System.out.printf("%s usage:\n", TITLE);
    System.out.printf("\tjava %s <input-filename> <timestamp> [output-filename]\n", Application.class.getName());
  }

  /**
   * Unit tests need to supply application instance.
   *
   * @param supplier Provides application instance for unit tests
   */
  static void setApplicationSupplier(Supplier<Application> supplier) {
    applicationSupplier = supplier;
  }
}
