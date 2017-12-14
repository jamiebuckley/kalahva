package com.github.jamiebuckley.kalahva.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {

  public enum State {
    WAITING, PLAYING, FINISHED
  }

  public enum Winner {
    ONE, TWO, DRAW
  }

  private Long id;

  private LocalDateTime created;

  private LocalDateTime updated;

  private Long playerOne;

  private Long playerTwo;

  //Java should have a bit type
  private boolean playerTurn;

  private List<Pit> pits;

  private State state = State.WAITING;

  private Winner winner;

  public Game() {
    pits = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

  public boolean getPlayerTurn() {
    return playerTurn;
  }

  public void setPlayerTurn(boolean playerTurn) {
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

  public Winner getWinner() {
    return winner;
  }

  public void setWinner(Winner winner) {
    this.winner = winner;
  }
}
