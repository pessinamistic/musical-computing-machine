/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String email) {
    super("User with email " + email + " not found");
  }
}
