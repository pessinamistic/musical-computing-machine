/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.exception;

public class EmailAlreadyExistsException extends RuntimeException {
  public EmailAlreadyExistsException(String email) {
    super("Email " + email + " is already registered");
  }
}
