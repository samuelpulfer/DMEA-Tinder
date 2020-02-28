CREATE TABLE event (
	id serial PRIMARY KEY,
	name VARCHAR(300),
	description VARCHAR(5000),
	starttime timestamp with time zone,
	endtime timestamp with time zone,
	status VARCHAR(20),
	externallink VARCHAR(500),
	place VARCHAR(100),
	created timestamp with time zone,
	modified timestamp with time zone,
	deleted timestamp with time zone
);

CREATE TABLE users (
	id VARCHAR(36) PRIMARY KEY,
	link VARCHAR(36),
	created timestamp with time zone,
	modified timestamp with time zone,
	deleted timestamp with time zone
);

CREATE TABLE preference (
	id serial PRIMARY KEY,
	userid VARCHAR(36) NOT NULL,
	event INTEGER NOT NULL,
	state INTEGER NOT NULL,
	created timestamp with time zone,
	modified timestamp with time zone,
	deleted timestamp with time zone
);

CREATE VIEW calendar AS (
	SELECT preference.userid, preference.state, event.name, event.description, event.starttime, event.endtime, event.status, event.externallink, event.place
	FROM preference
	LEFT JOIN event ON(preference.event = event.id)
	WHERE preference.deleted IS NULL AND event.deleted IS NULL
);

CREATE OR REPLACE FUNCTION userevent(VARCHAR(36)) RETURNS TABLE (
    id integer,
    name character varying(300),
    description character varying(5000),
    starttime timestamp with time zone,
    endtime timestamp with time zone,
    status character varying(20),
	state integer,
    externallink character varying(500),
    place character varying(100),
    created timestamp with time zone,
    modified timestamp with time zone,
    deleted timestamp with time zone
)  AS $$
 
BEGIN

	RETURN QUERY SELECT
		event.id,
		event.name,
		event.description,
		event.starttime,
		event.endtime,
		event.status,
		preference.state,
		event.externallink,
		event.place,
		event.created,
		event.modified,
		event.deleted
	FROM event
	LEFT JOIN preference ON(
		event.id = preference.event
		AND preference.userid = $1
		AND preference.deleted IS NULL
	)
	ORDER BY id asc;
END;
$$ LANGUAGE plpgsql;