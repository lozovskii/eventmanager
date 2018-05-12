import {
  Component, OnInit,
  ViewChild,
  TemplateRef
} from '@angular/core';
import {
  startOfDay,
  endOfDay,
  subDays,
  addDays,
  endOfMonth,
  isSameDay,
  isSameMonth,
  addHours
} from 'date-fns';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {EventService} from "../../_services"
import {Event} from "../../_models";

import { Subject } from 'rxjs';
import {
  CalendarEvent,
  CalendarEventAction,
  CalendarEventTimesChangedEvent,
  DAYS_OF_WEEK
} from 'angular-calendar';

const colors: any = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3'
  },
  blue: {
    primary: '#1e90ff',
    secondary: '#D1E8FF'
  }
};

@Component({
  selector: 'calendar-component',
  templateUrl: 'calendar.component.html'
})
export class CalendarComponent {
  constructor(private eventService: EventService,
              private modal: NgbModal) {}

  @ViewChild('modalContent') modalContent: TemplateRef<any>;
  viewDate = new Date();

  activeDayIsOpen: boolean = true;
  view: string = 'month';
  weekStartsOn: number = DAYS_OF_WEEK.MONDAY;
  events: Event[];

  calendarEvents: CalendarEvent[] = [];

  ngOnInit(): void {
    this.getEvents();
  }

  refresh: Subject<any> = new Subject();

  getEvents(): void {
    this.eventService.getEventsByCustId()
      .subscribe((events) => {
        this.events = events;
        for(let i = 0; i < events.length; i++) {
          console.log(this.events[i].visibility);
          console.log(this.events[i].startTime + ' ' + this.events[i].endTime)
          this.calendarEvents.push({
            title: this.events[i].name,
            start: new Date(this.events[i].startTime),
            end: new Date(this.events[i].endTime),
            color: this.events[i].visibility == "PUBLIC" ? colors.red : colors.blue
          })
        }
        this.refresh.next();
      });
  }
}
