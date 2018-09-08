package au.com.messagemedia.soccer.ex;

public class ValidationException extends Exception {
  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(String message, Exception cause) {
    super(message, cause);
  }
}
