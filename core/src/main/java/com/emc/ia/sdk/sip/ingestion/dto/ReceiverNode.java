/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sdk.sip.ingestion.dto;

import java.util.ArrayList;
import java.util.List;


public class ReceiverNode extends NamedLinkContainer {

  private String workingDirectory;
  private String logsStore;
  private List<Sip> sips = new ArrayList<>();

  public String getWorkingDirectory() {
    return workingDirectory;
  }

  public void setWorkingDirectory(String workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  public String getLogsStore() {
    return logsStore;
  }

  public void setLogsStore(String logsStore) {
    this.logsStore = logsStore;
  }

  public List<Sip> getSips() {
    return sips;
  }

  public void setSips(List<Sip> sips) {
    this.sips = sips;
  }

}