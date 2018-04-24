import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Event} from "../_models/event";
import {AlertService} from "../_services/alert.service";
import {Router} from "@angular/router";
import {EventService} from "../_services/event.service";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {
  event: Event;
  events: Event[];
  loading = false;
  eventForm: FormGroup;
  eventsForm: FormGroup;

  constructor(private router: Router,
              private eventService: EventService,
              private alertService: AlertService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.eventForm = this.formBuilder.group({
      name: [''],
      folderId: [''],
      creatorId: [''],
      day: [''],
      startTime: [''],
      endTime: [''],
      priority: [''],
      visibility: [''],
      frequencyValue: [''],
      frequency: [''],
      description: [''],
      status: ['']
    });
    this.eventsForm = this.formBuilder.group({});
  }

  create(eventFromForm: Event) {
    if((eventFromForm.day != null) && (eventFromForm.day != '')) {
      eventFromForm.startTime = eventFromForm.day + ' ' + eventFromForm.startTime + ':00';
      eventFromForm.endTime = eventFromForm.day + ' ' + eventFromForm.endTime + ':00';
      eventFromForm.frequency = eventFromForm.frequencyValue + '-' + eventFromForm.frequency;
    }
    console.log('event: ' + JSON.stringify(eventFromForm));
    this.loading = true;
    this.eventService.create(eventFromForm)
      .subscribe(
        data => {
          if(eventFromForm.day = '') {
            this.alertService.success('Event successfully created!', true);
            this.router.navigate(['/content']);
          }else{
            this.alertService.success('Note successfully created!', true);
            this.router.navigate(['/content']);
          }
        },
        error => {
          this.alertService.error(error);
          this.loading = false;
        });
  }

  getEvents(): void {
    this.eventService.getAllEvents()
      .subscribe((events) => this.events = events);
  }


}
