import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Event} from '../_models/event';
import {Observable} from 'rxjs/Observable';
import {UserService} from "./user.service";
import {EventDTOModel} from "../_models/dto/eventDTOModel";

@Injectable()
export class EventService {
  private eventsUrl = 'api/events';

  constructor(private http: HttpClient,
              private userService: UserService) {
  }

  create(event: EventDTOModel) {
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

  getEventById(eventId: string) {
    const url = `${this.eventsUrl}?eventId=${eventId}`;
    return this.http.get<Event>(url);
  }

  deleteEvent(eventId: string) {
    return this.http.post<number>('/api/events/delete', eventId);
  }

  addParticipant(eventId) {
    let customerId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/addParticipant?customerId=${customerId}&eventId=${eventId}`;
    return this.http.get(url);
  }

  removeParticipant(eventId) {
    let customerId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/removeParticipant?customerId=${customerId}&eventId=${eventId}`;
    return this.http.get(url);
  }

  isParticipant(customerId: string, eventId: string) {
    const url = `${this.eventsUrl}/isParticipant?customerId=${customerId}&eventId=${eventId}`;
    return this.http.get(url);
  }
}
