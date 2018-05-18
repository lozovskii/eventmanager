package com.ncgroup2.eventmanager.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
@PropertySource("classpath:google-calendar.properties")
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "EventManager";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FOLDER = "./backend/src/main/resources/credentials";
    private static com.google.api.services.calendar.Calendar client;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;

    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
//        InputStream in = CalendarQuickStart.class.getResourceAsStream(CLIENT_SECRET_DIR);
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        Details web = new Details();
        web.setClientId(clientId);
        System.out.println(clientId);
        web.setClientSecret(clientSecret);

        System.out.println(clientSecret);
        GoogleClientSecrets clientSecrets = new GoogleClientSecrets().setWeb(web);

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                clientSecrets,
                Collections.singletonList(CalendarScopes.CALENDAR_READONLY))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }



    public List<com.ncgroup2.eventmanager.entity.Event> getEvents(String calendarId, LocalDateTime from, LocalDateTime to) throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME)
                .build();
        DateTime min = new DateTime(from.toEpochSecond(ZoneOffset.UTC)*1000);
        DateTime max = new DateTime(to.toEpochSecond(ZoneOffset.UTC)*1000);
        System.out.println(calendarId);
        Events events = service.events().list(calendarId+"#holiday@group.v.calendar.google.com")
                .setTimeMin(min)
                .setTimeMax(max)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            return parseEvents(items);
        }


        return null;
    }

    private List<com.ncgroup2.eventmanager.entity.Event> parseEvents(List<Event> googleEvents) {
        List<com.ncgroup2.eventmanager.entity.Event> events = new LinkedList<>();
        googleEvents.forEach(googleEvent -> {
            System.out.println(googleEvent);
            com.ncgroup2.eventmanager.entity.Event event = new com.ncgroup2.eventmanager.entity.Event();
            event.setId(googleEvent.getId());
            event.setName(googleEvent.getSummary());
            event.setDescription(googleEvent.getDescription());
            parseDates(googleEvent,event);
            event.setVisibility("PUBLIC");
            System.out.println(event);
            ((LinkedList<com.ncgroup2.eventmanager.entity.Event>) events).push(event);
        });
        return events;
    }

    private void parseDates(Event googleEvent, com.ncgroup2.eventmanager.entity.Event event) {
        DateTime googleStartDate = Optional.ofNullable(googleEvent.getStart().getDateTime())
                .orElse(googleEvent.getStart().getDate());
        DateTime googleEndDate = Optional.ofNullable(googleEvent.getEnd().getDateTime())
                .orElse(googleEvent.getStart().getDate());

        LocalDateTime startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(googleStartDate.getValue()), ZoneId.of("Z"));
        event.setStartTime(startDate);

        LocalDateTime endDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(googleStartDate.getValue()), ZoneId.of("Z"));
        if (googleEndDate.isDateOnly()) {
            event.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(googleEndDate.getValue()), ZoneId.of("Z")));
            endDate = endDate.plusDays(1).minusSeconds(1);
        }

        event.setEndTime(endDate);
    }
}
