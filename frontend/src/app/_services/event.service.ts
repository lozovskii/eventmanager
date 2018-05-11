import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Event} from '../_models/event';
import {Observable} from 'rxjs';
import {UserService} from "./user.service";
import {EventDTOModel} from "../_models/dto/eventDTOModel";
import {AuthenticationService} from "./authentication.service";
import {UpdateEventDTO} from "../_models/dto/UpdateEventDTO";

@Injectable()
export class EventService {
  private eventsUrl = 'api/events';

  constructor(private http: HttpClient,
              private userService: UserService) {
  }

  create(event: EventDTOModel) {
    console.log('here: ' + JSON.stringify(event));
    return this.http.post<Event>('/api/events', event, {headers: AuthenticationService.getAuthHeader()});
  }

  getAllEvents(): Observable<Event[]> {
    let customerId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/public_and_friends?customerId=${customerId}`;
    return this.http.get<Event[]>(url, {headers: AuthenticationService.getAuthHeader()});
  }

  getEventsByCustId(): Observable<Event[]> {
    let custId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/my${custId}`;
    return this.http.get<Event[]>(url, {headers: AuthenticationService.getAuthHeader()})
  }

  getDraftsByCustId():Observable<Event[]> {
    let custId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/drafts${custId}`;
    return this.http.get<Event[]>(url, {headers: AuthenticationService.getAuthHeader()})
  }

  getNotesByCustId():Observable<Event[]> {
    let custId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/notes${custId}`;
    return this.http.get<Event[]>(url, {headers: AuthenticationService.getAuthHeader()})
  }

  getInvitesByCustId():Observable<Event[]> {
    let custId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/invites${custId}`;
    return this.http.get<Event[]>(url, {headers: AuthenticationService.getAuthHeader()})
  }

  getEventById(eventId: string) {
    const url = `${this.eventsUrl}?eventId=${eventId}`;
    return this.http.get<EventDTOModel>(url, {headers: AuthenticationService.getAuthHeader()});
  }

  deleteEvent(eventId: string) {
    return this.http.post<number>('/api/events/delete', eventId, {headers: AuthenticationService.getAuthHeader()});
  }

  updateEvent(updateEventDTO: UpdateEventDTO) {
    console.log("updateEvent(eventDTO: EventDTOModel): " + JSON.stringify(updateEventDTO));
    return this.http.put<EventDTOModel>('/api/events/update', updateEventDTO, {headers: AuthenticationService.getAuthHeader()});
  }

  addParticipant(eventId) {
    let customerId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/addParticipant?customerId=${customerId}&eventId=${eventId}`;
    return this.http.get(url, {headers: AuthenticationService.getAuthHeader()});
  }

  removeParticipant(eventId) {
    let customerId = this.userService.getCurrentId();
    const url = `${this.eventsUrl}/removeParticipant?customerId=${customerId}&eventId=${eventId}`;
    return this.http.get(url, {headers: AuthenticationService.getAuthHeader()});
  }

  isParticipant(customerId: string, eventId: string) {
    const url = `${this.eventsUrl}/isParticipant?customerId=${customerId}&eventId=${eventId}`;
    return this.http.get(url, {headers: AuthenticationService.getAuthHeader()});
  }
}
