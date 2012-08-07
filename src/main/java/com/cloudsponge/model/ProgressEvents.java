package com.cloudsponge.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Object for monitoring the import process progress.<br>
 * Contains {@link Event} representing each step of the process:
 * <ul>
 * 	<li>{@link EventType#INITIALIZING}
 *  <li>{@link EventType#GATHERING}
 *  <li>{@link EventType#COMPLETE}
 * </ul>
 * With each of its statuses:
 * <ul>
 * 	<li>{@link EventStatus#INPROGRESS}
 * 	<li>{@link EventStatus#COMPLETED}
 * 	<li>{@link EventStatus#ERROR}
 * </ul>
 * 
 * @author andrenpaes
 * @see EventType
 * @see EventStatus
 */
public class ProgressEvents extends CloudSpongeResponse {

	private static final long serialVersionUID = 7853343848740582120L;

	public enum EventType {
		INITIALIZING, GATHERING, COMPLETE;

		public static EventType toEventType(String type) {
			for (EventType eventType : values()) {
				if (eventType.name().equalsIgnoreCase(type)) {
					return eventType;
				}
			}
			return null;
		}
	}

	public enum EventStatus {
		INPROGRESS, COMPLETED, ERROR;

		public static EventStatus toEventStatus(String status) {
			for (EventStatus eventStatus : values()) {
				if (eventStatus.name().equalsIgnoreCase(status)) {
					return eventStatus;
				}
			}
			return null;
		}
	}

	private final Map<EventType, Event> events = new LinkedHashMap<EventType, Event>();

	public void addEvent(Event event) {
		events.put(event.getType(), event);
	}

	public boolean isDone() {
		Event completeEvent = getEvent(EventType.COMPLETE);
		if (completeEvent == null) {
			return false;
		}

		return completeEvent.getStatus() == EventStatus.COMPLETED
				|| completeEvent.getStatus() == EventStatus.ERROR;
	}

	public List<Event> getEvents() {
		return new ArrayList<Event>(events.values());
	}

	public Event getEvent(EventType eventType) {
		return events.get(eventType);
	}

	public Event getCurrentInProgressEvent() {
		for (Event event : getEvents()) {
			if (event.getStatus() == EventStatus.INPROGRESS) {
				return event;
			}
		}

		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof ProgressEvents && super.equals(obj)) {
			final ProgressEvents that = (ProgressEvents) obj;
			return new EqualsBuilder().append(this.getEvents(),
					that.getEvents()).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(341211, 1121).append(this.getImportId())
				.append(this.getEvents()).toHashCode();
	}

	@Override
	protected ToStringBuilder toStringBuilder() {
		return super.toStringBuilder().append("events", this.getEvents());
	}

	public static class Event {
		private EventType type;

		private EventStatus status;

		private int value = 0;

		public Event() {
		}

		public Event(String type, String status, String value) {
			this.type = EventType.toEventType(type);
			this.status = EventStatus.toEventStatus(status);

			try {
				this.value = Integer.parseInt(value);
			} catch (NumberFormatException e) {
			}
		}

		public EventType getType() {
			return type;
		}

		public void setType(EventType type) {
			this.type = type;
		}

		public EventStatus getStatus() {
			return status;
		}

		public void setStatus(EventStatus status) {
			this.status = status;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof Event) {
				Event that = (Event) obj;
				return new EqualsBuilder()
						.append(this.getType(), that.getType())
						.append(this.getStatus(), that.getStatus())
						.append(this.getValue(), that.getValue()).isEquals();
			}
			return false;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(this.getType())
					.append(this.getStatus()).append(this.getValue())
					.toHashCode();
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
					.append("type", this.getType())
					.append("status", this.getStatus())
					.append("value", this.getValue()).toString();
		}
	}
}
