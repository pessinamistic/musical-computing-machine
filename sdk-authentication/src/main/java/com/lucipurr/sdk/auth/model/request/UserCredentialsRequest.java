/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.model.request;

public interface UserCredentialsRequest {
  String username();

  String password();

  String email();
}
