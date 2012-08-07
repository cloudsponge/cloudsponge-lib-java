package com.cloudsponge.model;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.cloudsponge.model.ProgressEvents.Event;
import com.cloudsponge.model.ProgressEvents.EventType;

public class ProgressEventsTest {

	private ProgressEvents progressEvents;

	@Before
	public void setUp() throws Exception {
		progressEvents = new ProgressEvents();
	}

	@Test
	public void addEvent() {
		final Event initializingEvent = new Event("INITIALIZING", "COMPLETED", "1");
		progressEvents.addEvent(initializingEvent);
		final Event gatheringEvent = new Event("GATHERING", "COMPLETED", "1");
		progressEvents.addEvent(gatheringEvent);
		final Event completeEvent = new Event("COMPLETE", "COMPLETED", "1");
		progressEvents.addEvent(completeEvent);

		assertEquals(initializingEvent,
				progressEvents.getEvent(EventType.INITIALIZING));
		assertEquals(gatheringEvent,
				progressEvents.getEvent(EventType.GATHERING));
		assertEquals(completeEvent,
				progressEvents.getEvent(EventType.COMPLETE));
	}

	@Test
	public void addEvent_only_one_for_each_type() {
		final Event event = new Event("INITIALIZING", "INPROGRESS", "1");
		progressEvents.addEvent(event);
		progressEvents.addEvent(event);

		assertEquals(event, progressEvents.getEvent(EventType.INITIALIZING));
		assertEquals(Arrays.asList(event), progressEvents.getEvents());
	}

	@Test
	public void currentInProgressEvent() {
		final Event initializingEvent = new Event("INITIALIZING", "COMPLETED", "1");
		progressEvents.addEvent(initializingEvent);
		final Event gatheringEvent = new Event("GATHERING", "INPROGRESS", "1");
		progressEvents.addEvent(gatheringEvent);

		assertEquals(Arrays.asList(initializingEvent, gatheringEvent),
				progressEvents.getEvents());
		assertEquals(gatheringEvent, progressEvents.getCurrentInProgressEvent());
	}

	@Test
	public void isDone_no_complete_event() {
		final Event completeEvent = new Event("INITIALIZING", "INPROGRESS", "1");
		progressEvents.addEvent(completeEvent);
		
		assertFalse(progressEvents.isDone());
	}

	@Test
	public void isDone_not_yet_complete() {
		final Event completeEvent = new Event("COMPLETE", "INPROGRESS", "1");
		progressEvents.addEvent(completeEvent);

		assertFalse(progressEvents.isDone());
	}

	@Test
	public void isDone_completed_with_errors() {
		final Event completeEvent = new Event("COMPLETE", "ERROR", "1");
		progressEvents.addEvent(completeEvent);
		
		assertTrue(progressEvents.isDone());
	}

	@Test
	public void isDone_completed_successfully() {
		final Event completeEvent = new Event("COMPLETE", "COMPLETED", "1");
		progressEvents.addEvent(completeEvent);
		
		assertTrue(progressEvents.isDone());
	}
}
