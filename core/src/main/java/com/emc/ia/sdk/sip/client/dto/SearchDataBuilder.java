/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sdk.sip.client.dto;

import com.emc.ia.sdk.support.xml.XmlBuilder;


public final class SearchDataBuilder {

  private final XmlBuilder builder;

  private SearchDataBuilder() {
    builder = XmlBuilder.newDocument()
      .element("data");
  }

  public static SearchDataBuilder builder() {
    return new SearchDataBuilder();
  }

  private SearchDataBuilder criterion(String field, String operator, String value) {
    builder.element("criterion")
        .element("name", field)
        .element("operator", operator)
        .element("value", value)
        .end();
    return this;
  }

  private SearchDataBuilder criterion(String field, String operator, String value1, String value2) {
    builder.element("criterion")
        .element("name", field)
        .element("operator", operator)
        .element("value", value1)
        .element("value", value2)
        .end();
    return this;
  }

  public SearchDataBuilder equal(String field, String value) {
    return criterion(field, "EQUAL", value);
  }

  public SearchDataBuilder notEqual(String field, String value) {
    return criterion(field, "NOT_EQUAL", value);
  }

  public SearchDataBuilder startsWith(String field, String value) {
    return criterion(field, "STARTS_WITH", value);
  }

  public SearchDataBuilder endsWith(String field, String value) {
    return criterion(field, "ENDS_WITH", value);
  }

  public SearchDataBuilder between(String field, String value1, String value2) {
    return criterion(field, "BETWEEN", value1, value2);
  }

  public SearchDataBuilder contains(String field, String value) {
    return criterion(field, "CONTAINS", value);
  }

  public String build() {
    return builder.end().toString();
  }

}
