/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sdk.sip.client;

import java.io.Closeable;
import java.io.InputStream;

public interface QueryResult extends Closeable {

  int getResultSetQuota();

  int getAiuQuota();

  int getResultSetCount();

  int getAipQuota();

  boolean isCacheOutAipIgnored();

  InputStream getResultStream();

}
