package com.bitlake.backend.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String identifier) {
    super(String.format("Resource with identifier %s not found", identifier));
  }
}
