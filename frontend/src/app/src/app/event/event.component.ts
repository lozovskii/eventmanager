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
  // loading = false;
  eventForm: FormGroup;

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
  }

  create(eventFromForm: Event) {
    eventFromForm.startTime = eventFromForm.day + ' ' + eventFromForm.startTime + ':00';
    eventFromForm.endTime = eventFromForm.day + ' ' + eventFromForm.endTime + ':00';
    eventFromForm.frequency =  eventFromForm.frequencyValue + '-' +  eventFromForm.frequency;
    console.log('event: ' + JSON.stringify(eventFromForm));
    // this.loading = true;
    this.eventService.create(eventFromForm)
      .subscribe(
        data => {
          // this.alertService.success('Registration successful', true);
          this.router.navigate(['/content']);
        },
        error => {
          this.alertService.error(error);
          // this.loading = false;
        });
  }
}
