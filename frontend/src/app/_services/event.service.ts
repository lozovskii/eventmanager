import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Event } from '../_models/event';
import {Observable} from 'rxjs/Observable';
import {UserService} from "./user.service";

@Injectable()
export class EventService {
  private eventsUrl = 'api/events';
  private notesUrl = 'api/notes';

  constructor(private http: HttpClient,
              private userService : UserService) {
  }

  create(event: Event) {
    console.log('here: ' + JSON.stringify(event));
    return this.http.post<Event>('/api/events', event);
  }

  getAllEvents(): Observable<Event[]> {
    let customerId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/public_and_friends?customerId=${customerId}`;
    return this.http.get<Event[]>(url);
  }

  getEventsByCustId(): Observable<Event[]> {
    let custId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/${custId}`;
    return this.http.get<Event[]>(url)
  }

  getNotesByCustId(): Observable<Event[]> {
    let custId = this.userService.getCurrentId();
    const url = `${this.notesUrl}/${custId}`;
    return this.http.get<Event[]>(url)
  }

  getEventById(eventId: string) {
    const url = `${this.eventsUrl}?eventId=${eventId}`;
    return this.http.get<Event>(url);
  }

  isParticipant(currentUserId: string, eventId: string) {
    const url = `${this.eventsUrl}/isParticipant?customerId=${currentUserId}&eventId=${eventId}`;
    return this.http.get(url);
  }

  addParticipant(eventId){
    let customerId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/addParticipant?customerId=${customerId}&eventId=${eventId}`;
      return this.http.get(url);
  }

  removeParticipant(eventId){
    let customerId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/removeParticipant?customerId=${customerId}&eventId=${eventId}`;
    return this.http.get(url);
  }
}
