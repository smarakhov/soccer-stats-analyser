package au.com.messagemedia.soccer;

import au.com.messagemedia.soccer.model.MatchEvent;
import au.com.messagemedia.soccer.service.ParserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class Application {

  private static final String TITLE = "Soccer Match Stats Analyser";

  private ParserService parserService;

  void runAnalyser(String inputFilename, String timestamp, String outputFilename) throws IOException {
    log.debug("runAnalyser({}, {}, {})", inputFilename, timestamp, outputFilename);

    List<MatchEvent> matchEvents = parserService.parse(inputFilename);
    log.debug("matchEvents: {}", matchEvents);
    // TODO: analyze events
    // TODO: print report
  }

  public static void main(String[] args) {
    log.info("{} started", TITLE);

    if (args.length < 2 || args.length > 3) {
      printUsage();
      System.exit(1);
    }

    try {
      new Application(new ParserService()).runAnalyser(args[0], args[1], args.length > 2 ? args[2] : null);
    } catch (IOException ioException) {
      log.error("Error while parsing the file [{}]: {}", args[0], ioException.getMessage());
      System.exit(1);
    }
  }

  private static void printUsage() {
    System.out.printf("%s usage:\n", TITLE);
    System.out.printf("\tjava %s <input-filename> <timestamp> [output-filename]\n", Application.class.getName());
  }
}
