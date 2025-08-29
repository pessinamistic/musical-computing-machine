/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.exception;

public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(String username) {
    super("Email " + username + " is already registered");
  }
}
