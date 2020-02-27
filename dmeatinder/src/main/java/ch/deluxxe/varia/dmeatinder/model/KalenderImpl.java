package ch.deluxxe.varia.dmeatinder.model;

import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

import ch.deluxxe.varia.dmeatinder.model.iface.Kalender;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.parameter.FmtType;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.model.property.Categories;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;

public class KalenderImpl implements Kalender {
	private static final String PRODUKT_ID = "-//DMEA 2020//dmea.deluxxe.ch 1.0//DE";
	private UidGenerator uidGenerator = null;
	private TimeZone timezone = null;
	private Calendar icsCalendar = null;
	
	public KalenderImpl() {
		uidGenerator = new RandomUidGenerator();
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		timezone = registry.getTimeZone("Europe/Berlin");
		icsCalendar = new Calendar();
		icsCalendar.getProperties().add(new ProdId(PRODUKT_ID));
		icsCalendar.getProperties().add(Version.VERSION_2_0);
		icsCalendar.getProperties().add(CalScale.GREGORIAN);
	}

	@Override
	public void add(ZonedDateTime start, ZonedDateTime end, String title, String comment, String categories, String place, String url) {
		DateTime startDate = new DateTime(GregorianCalendar.from(start).getTime());
		DateTime endDate = new DateTime(GregorianCalendar.from(end).getTime());
		VEvent event = new VEvent(startDate, endDate, title);
		event.getProperties().add(new Description(comment));
		ParameterList pl = new ParameterList();
		pl.add(new FmtType("text/html"));
		comment += "<br><a href=\"" + url + "\">Mehr Infos</a>";
		event.getProperties().add(new XProperty("X-ALT-DESC", pl, comment));
		event.getProperties().add(timezone.getVTimeZone().getTimeZoneId());
		event.getProperties().add(uidGenerator.generateUid());
		event.getProperties().add(new Categories(categories));
		event.getProperties().add(new Location(pl, place));
		icsCalendar.getComponents().add(event);
	}

	@Override
	public String getCalendar() {		
		return icsCalendar.toString();
	}
}
