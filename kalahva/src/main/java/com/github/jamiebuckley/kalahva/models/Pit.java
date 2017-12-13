package com.github.jamiebuckley.kalahva.models;

public class Pit {

  public enum Type {
    HOUSE, STORE
  }

  private String owner;

  private int seeds;

  private Type type = Type.HOUSE;

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
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
