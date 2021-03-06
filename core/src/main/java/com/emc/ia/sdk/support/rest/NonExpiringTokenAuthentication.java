/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sdk.support.rest;

import java.util.Optional;

import com.emc.ia.sdk.support.http.Header;

public final class NonExpiringTokenAuthentication implements AuthenticationStrategy {

  private final String token;

  public static Optional<AuthenticationStrategy> optional(String token, String user, String password) {
    if ((user == null) && (password == null) && (token != null)) {
      return Optional.of(new NonExpiringTokenAuthentication(token));
    } else {
      return Optional.empty();
    }
  }

  public NonExpiringTokenAuthentication(String token) {
    if (token.isEmpty()) {
      throw new IllegalArgumentException("Token is empty");
    } else {
      this.token = "Bearer " + token;
    }
  }

  @Override
  public Header issueAuthHeader() {
    return new Header("Authorization", token);
  }
}
