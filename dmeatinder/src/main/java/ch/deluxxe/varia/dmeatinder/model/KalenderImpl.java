package ch.deluxxe.varia.dmeatinder.model;

import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

import ch.deluxxe.varia.dmeatinder.model.iface.Kalender;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;

public class KalenderImpl implements Kalender {
	private static final String PRODUKT_ID = "-//DMEA 2020//dmea.deluxxe.ch 1.0//DE";
	private UidGenerator uidGenerator = null;
	public KalenderImpl() {
		uidGenerator = new RandomUidGenerator();
	}

	@Override
	public void add(ZonedDateTime start, ZonedDateTime end, String title, String comment) {
		java.util.Calendar startDate = GregorianCalendar.from(start);
		java.util.Calendar endDate = GregorianCalendar.from(end);
		
	}

}
