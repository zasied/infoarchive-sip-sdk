/*
 * Copyright (c) 2016 EMC Corporation. All Rights Reserved.
 */
package com.emc.ia.sdk.sip.assembly;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;

import com.emc.ia.sdk.support.datetime.Clock;
import com.emc.ia.sdk.support.test.TestCase;


@SuppressWarnings("unchecked")
public class WhenAssemblingSipsInTimeWindows extends TestCase {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();
  private final SipAssembler<String> assembler = mock(SipAssembler.class);
  private final SipSegmentationStrategy<String> segmentationStrategy = mock(SipSegmentationStrategy.class);
  private final Clock clock = mock(Clock.class);
  private final Consumer<FileGenerationMetrics> callback = mock(Consumer.class);
  private long maxTime;
  private SipAssemblyTimer timer;
  private File sipDir;
  private TimeBasedBatchSipAssembler<String> batchAssembler;
  private String taskName;
  private Runnable alarm;

  @Before
  public void init() throws IOException {
    PackagingInformationFactory factory = mock(PackagingInformationFactory.class);
    when(assembler.getPackagingInformationFactory()).thenReturn(factory);
    sipDir = temporaryFolder.newFolder();
    maxTime = randomInt(37, 313);
    timer = new SipAssemblyTimer(maxTime, clock, callback);
    batchAssembler = new TimeBasedBatchSipAssembler<>(assembler, segmentationStrategy, sipDir, timer);
    ArgumentCaptor<String> taskNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(clock).schedule(taskNameCaptor.capture(), eq(maxTime), eq(TimeUnit.MILLISECONDS), taskCaptor.capture());
    taskName = taskNameCaptor.getValue();
    alarm = taskCaptor.getValue();
  }

  @Test
  public void shouldResetTimerWhenObjectAdded() throws IOException {
    batchAssembler.add(randomString());

    verify(clock).cancel(taskName);
    verify(clock, times(2)).schedule(taskName, maxTime, TimeUnit.MILLISECONDS, alarm);
  }

  @Test
  public void shouldCallBackWhenSipIsFullAfterAddingObject() throws IOException {
    String domainObject1 = randomString();
    String domainObject2 = randomString();
    when(segmentationStrategy.shouldStartNewSip(eq(domainObject2), any(SipMetrics.class))).thenReturn(true);
    SipMetrics sipMetrics = new SipMetrics(null);
    when(assembler.getMetrics()).thenReturn(sipMetrics);

    batchAssembler.add(domainObject1);
    batchAssembler.add(domainObject2);

    verify(assembler).add(domainObject1);
    verify(assembler).add(domainObject2);
    ArgumentCaptor<FileGenerationMetrics> metricsCaptor = ArgumentCaptor.forClass(FileGenerationMetrics.class);
    verify(callback).accept(metricsCaptor.capture());
    FileGenerationMetrics metrics = metricsCaptor.getValue();
    assertEquals("SIP directory", sipDir, metrics.getFile().getParentFile());
    assertSame("SIP metrics", sipMetrics, metrics.getMetrics());
  }

  @Test
  public void shouldCallBackAfterTimePassedWhenSipIsNonEmpty() throws IOException {
    batchAssembler.add(randomString());

    alarm.run();

    verify(callback).accept(isNotNull(FileGenerationMetrics.class));
  }

  @Test
  public void shouldNotCallBackAfterTimePassedWhenSipIsEmpty() throws IOException {
    alarm.run();

    verify(callback, never()).accept(any(FileGenerationMetrics.class));
  }

  @Test
  public void shouldStopTimerWhenClosed() throws IOException {
    batchAssembler.end();

    verify(clock).cancel(taskName);
  }

}