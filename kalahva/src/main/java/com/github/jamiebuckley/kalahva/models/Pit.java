package com.github.jamiebuckley.kalahva.models;

public class Pit {

  public enum Type {
    HOUSE, STORE
  }

  private boolean owner;

  private int seeds;

  private Type type = Type.HOUSE;

  public boolean getOwner() {
    return owner;
  }

  public void setOwner(boolean owner) {
    this.owner = owner;
  }

  public int getSeeds() {
    return seeds;
  }

  public void setSeeds(int seeds) {
    this.seeds = seeds;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }
}
