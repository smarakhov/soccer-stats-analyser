package au.com.messagemedia.soccer.model;

import lombok.Getter;

public enum MatchEventType {
  START(true),
  POSSESS(true),
  SHOT(true),
  SCORE(true),
  BREAK(false),
  END(false);

  @Getter
  private boolean hasTeam;

  MatchEventType(boolean hasTeam) {
    this.hasTeam = hasTeam;
  }
}
