package ch.deluxxe.varia.dmeatinder.model.iface;

import java.time.ZonedDateTime;

public interface Kalender {
	
	public void add(ZonedDateTime start, ZonedDateTime end, String title, String comment);

}
