import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AlertService} from "../_services/alert.service";
import {Router} from "@angular/router";
import {EventService} from "../_services/event.service";
import {UserService} from "../_services/user.service";

@Component({
  selector: 'app-update-event',
  templateUrl: './update-event.component.html',
  styleUrls: ['./update-event.component.css']
})
export class UpdateEventComponent implements OnInit {
  eventForm: FormGroup;
  constructor(private router: Router,
              private eventService: EventService,
              private alertService: AlertService,
              private formBuilder: FormBuilder,
              private userService : UserService) { }

  ngOnInit(): void {
    this.eventForm = this.formBuilder.group({
      name: [''],
      groupId: [''],
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

}
