package au.com.messagemedia.soccer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Slf4j
public class WriterService {

  PrintWriter getWriter(String filename) throws FileNotFoundException {
    log.debug("getWriter({})", filename);

    return StringUtils.isBlank(filename)
        ? new PrintWriter(System.out)
        : new PrintWriter(new File(filename));
  }
}
