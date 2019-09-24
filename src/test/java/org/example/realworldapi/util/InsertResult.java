package org.example.realworldapi.util;

import java.util.LinkedList;
import java.util.List;

public class InsertResult<E> {

  private List<E> results;
  private int nextValue;

  public InsertResult() {
    this.results = new LinkedList<>();
    this.nextValue = 0;
  }

  public int add(E item) {
    this.results.add(item);
    return nextValue++;
  }

  public List<E> getResults() {
    return this.results;
  }

  public int getNextValue() {
    return this.nextValue;
  }
}
