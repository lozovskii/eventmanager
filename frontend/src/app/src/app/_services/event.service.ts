import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Event } from '../_models/event';

@Injectable()
export class EventService {
  constructor(private http: HttpClient) {
  }

  create(event: Event) {
    console.log('here: ' + JSON.stringify(event));
    return this.http.post<Event>('/api/events', event);
  }

}
