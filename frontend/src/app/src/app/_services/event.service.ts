import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Event } from '../_models/event';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class EventService {
  private eventsUrl = 'api/events';

  constructor(private http: HttpClient) {
  }

  create(event: Event) {
    console.log('here: ' + JSON.stringify(event));
    return this.http.post<Event>('/api/events', event);
  }

  getAllEvents(): Observable<Event[]> {

    return this.http.get<Event[]>(this.eventsUrl)
  }
}
