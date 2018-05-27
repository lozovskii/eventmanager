import {Component, Input, TemplateRef, ViewChild} from '@angular/core';
import {addDays, addHours, endOfDay, endOfMonth, isSameDay, isSameMonth, startOfDay, subDays} from 'date-fns';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {EventService} from "../../_services"
import {Event} from "../../_models";
import {ActivatedRoute, Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {Subject} from 'rxjs';
import {CalendarEvent, DAYS_OF_WEEK} from 'angular-calendar';
import {EventDTOModel} from "../../_models/dto/eventDTOModel";

const colors: any = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3'
  },
  blue: {
    primary: '#1e90ff',
    secondary: '#D1E8FF'
  },
  pink: {
    primary: "#9b16dd",
    secondary: "#e6bdf9"
  },
  green: {
    primary: '#86af49',
    secondary: '#e3eaa7'
  }
};

@Component({
  selector: 'calendar-component',
  templateUrl: 'calendar.component.html'
})
export class CalendarComponent {
  @Input() type: string;
  @Input() login: string;

  isMy: boolean;

  holidays_list = [
    ['Australian Holidays', 'en.australian#holiday@group.v.calendar.google.com'],
    ['Austrian Holidays', 'en.austrian#holiday@group.v.calendar.google.com'],
    ['Brazilian Holidays', 'en.brazilian#holiday@group.v.calendar.google.com'],
    ['Canadian Holidays', 'en.canadian#holiday@group.v.calendar.google.com'],
    ['China Holidays', 'en.china#holiday@group.v.calendar.google.com'],
    ['Christian Holidays', 'en.christian#holiday@group.v.calendar.google.com'],
    ['Danish Holidays', 'en.danish#holiday@group.v.calendar.google.com'],
    ['Dutch Holidays', 'en.dutch#holiday@group.v.calendar.google.com'],
    ['Finnish Holidays', 'en.finnish#holiday@group.v.calendar.google.com'],
    ['French Holidays', 'en.french#holiday@group.v.calendar.google.com'],
    ['German Holidays', 'en.german#holiday@group.v.calendar.google.com'],
    ['Greek Holidays', 'en.greek#holiday@group.v.calendar.google.com'],
    ['Hong Kong (C) Holidays', 'en.hong_kong_c#holiday@group.v.calendar.google.com'],
    ['Hong Kong Holidays', 'en.hong_kong#holiday@group.v.calendar.google.com'],
    ['Indian Holidays', 'en.indian#holiday@group.v.calendar.google.com'],
    ['Indonesian Holidays', 'en.indonesian#holiday@group.v.calendar.google.com'],
    ['Iranian Holidays', 'en.iranian#holiday@group.v.calendar.google.com'],
    ['Irish Holidays', 'en.irish#holiday@group.v.calendar.google.com'],
    ['Islamic Holidays', 'en.islamic#holiday@group.v.calendar.google.com'],
    ['Italian Holidays', 'en.italian#holiday@group.v.calendar.google.com'],
    ['Japanese Holidays', 'en.japanese#holiday@group.v.calendar.google.com'],
    ['Jewish Holidays', 'en.jewish#holiday@group.v.calendar.google.com'],
    ['Malaysian Holidays', 'en.malaysia#holiday@group.v.calendar.google.com'],
    ['Mexican Holidays', 'en.mexican#holiday@group.v.calendar.google.com'],
    ['New Zealand Holidays', 'en.new_zealand#holiday@group.v.calendar.google.com'],
    ['Norwegian Holidays', 'en.norwegian#holiday@group.v.calendar.google.com'],
    ['Philippines Holidays', 'en.philippines#holiday@group.v.calendar.google.com'],
    ['Polish Holidays', 'en.polish#holiday@group.v.calendar.google.com'],
    ['Portuguese Holidays', 'en.portuguese#holiday@group.v.calendar.google.com'],
    ['Russian Holidays', 'en.russian#holiday@group.v.calendar.google.com'],
    ['Singapore Holidays', 'en.singapore#holiday@group.v.calendar.google.com'],
    ['South Africa Holidays', 'en.sa#holiday@group.v.calendar.google.com'],
    ['South Korean Holidays', 'en.south_korea#holiday@group.v.calendar.google.com'],
    ['Spain Holidays', 'en.spain#holiday@group.v.calendar.google.com'],
    ['Swedish Holidays', 'en.swedish#holiday@group.v.calendar.google.com'],
    ['Taiwan Holidays', 'en.taiwan#holiday@group.v.calendar.google.com'],
    ['Thai Holidays', 'en.thai#holiday@group.v.calendar.google.com'],
    ['UK Holidays', 'en.uk#holiday@group.v.calendar.google.com'],
    ['Ukrainian Holidays', 'en.ukrainian#holiday@group.v.calendar.google.com'],
    ['US Holidays', 'en.usa#holiday@group.v.calendar.google.com']];

  selectedHoliday: string;
  @ViewChild('modalContent') modalContent: TemplateRef<any>;
  viewDate = new Date();
  privates: boolean = false;
  publics: boolean = false;
  friends: boolean = false;
  allevents: boolean = true;
  modalData: {
    action: string;
    event: CalendarEvent;
  };
  activeDayIsOpen: boolean = false;
  view: string = 'month';
  weekStartsOn: number = DAYS_OF_WEEK.MONDAY;
  events: EventDTOModel[];
  nationalEvents: Event[];
  calendarEvents: CalendarEvent[] = [];
  refresh: Subject<any> = new Subject();

  constructor(private eventService: EventService,
              private router: Router,
              private forms: FormsModule,
              private activatedRoute: ActivatedRoute,
              private modal: NgbModal) {
  }

  ngOnInit(): void {
    if (this.type == 'my') {
      this.isMy = true;
    } else if (this.type == 'timeline') {
      this.isMy = false;
    }
    this.getEvents('ALL')

  }

  dayClicked({date, events}: { date: Date; events: CalendarEvent[] }): void {
    if (this.isMy && isSameMonth(date, this.viewDate)) {
      if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
      ) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
        this.viewDate = date;
      }
    }
  }

  handleEvent(action: string, event: CalendarEvent): void {
    if(this.isMy) {
      this.router.navigate(['/event-container', event.id]);
    }
  }

  queryEvents(eventType: string) {
    if (this.isMy) {
      if (eventType == 'PUBLIC') {
        return this.eventService.getAllPublicEventsInMonth()
      } else {
        if (eventType == 'PRIVATE') {
          return this.eventService.getAllPrivateEventsInMonth()
        } else {
          if (eventType == 'FRIENDS') {
            return this.eventService.getAllFriendsEventsInMonth()
          } else {
            return this.eventService.getEventsByCustId();
          }
        }
      }
    } else {
      return this.eventService.getTimeline(this.login);
    }
  }

  onChangePrivate(type: string): void {
    console.log(type);
    this.getEvents(type);
  }

  onChangePublic(type: string): void {
    console.log(type);
    this.getEvents(type);
  }

  onChangeFriends(type: string): void {
    console.log(type);
    this.getEvents(type);
  }

  onChangeAll(type: string): void {
    console.log(type);
    this.getEvents(type);
  }

  getEvents(type: string): void {
    this.queryEvents(type)
      .subscribe((events) => {
        this.events = events;
        this.calendarEvents = [];
        for (let i = 0; i < events.length; i++) {
          console.log(this.events[i].event.visibility);
          console.log(this.events[i].event.startTime + ' ' + this.events[i].event.endTime);
          this.calendarEvents.push({
            id: this.events[i].event.id,
            title: this.events[i].event.name,
            start: new Date(this.events[i].event.startTime),
            end: new Date(this.events[i].event.endTime),
            color: this.events[i].event.visibility == "PUBLIC" ? colors.red : this.events[i].event.visibility == "FRIENDS" ? colors.blue : colors.pink
          })
        }
        this.refresh.next();
      });
  }


  showHolidays() {
    this.calendarEvents = [];
    this.events.forEach((event) => {
      this.calendarEvents.push({
        id: event.event.id,
        title: event.event.name,
        start: new Date(event.event.startTime),
        end: new Date(event.event.endTime),
        color: event.event.visibility == "PUBLIC" ? colors.red : event.event.visibility == "FRIENDS" ? colors.blue : colors.pink
      })
    });
    this.refresh.next();
    console.log(this.selectedHoliday);
    this.eventService.getNationalEvents(this.selectedHoliday)
      .subscribe((events) => {
        this.nationalEvents = events;
        events.forEach((event) => {
          this.calendarEvents.push({
            id: event.id,
            title: event.name,
            start: new Date(event.startTime),
            end: new Date(event.endTime),
            color: colors.green
          })
        });
        this.refresh.next();
      });
  }

}
