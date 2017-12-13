package com.github.jamiebuckley.kalahva.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {

  public enum State {
    WAITING, PLAYING, FINISHED
  }

  private Long id;

  private LocalDateTime created;

  private LocalDateTime updated;

  private Long playerOne;

  private Long playerTwo;

  private Long playerTurn;

  private List<Pit> pits;

  private State state = State.WAITING;

  private String winner;

  public Game() {
    pits = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  public LocalDateTime getUpdated() {
    return updated;
  }

  public void setUpdated(LocalDateTime updated) {
    this.updated = updated;
  }

  public Long getPlayerOne() {
    return playerOne;
  }

  public void setPlayerOne(Long playerOne) {
    this.playerOne = playerOne;
  }

  public Long getPlayerTwo() {
    return playerTwo;
  }

  public void setPlayerTwo(Long playerTwo) {
    this.playerTwo = playerTwo;
  }

  public Long getPlayerTurn() {
    return playerTurn;
  }

  public void setPlayerTurn(Long playerTurn) {
    this.playerTurn = playerTurn;
  }

  public List<Pit> getPits() {
    return pits;
  }

  public void setPits(List<Pit> pits) {
    this.pits = pits;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public String getWinner() {
    return winner;
  }

  public void setWinner(String winner) {
    this.winner = winner;
  }
}
