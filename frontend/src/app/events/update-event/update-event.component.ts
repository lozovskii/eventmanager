import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AlertService} from "../../_services/alert.service";
import {ActivatedRoute, Router} from "@angular/router";
import {EventService} from "../../_services/event.service";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";

@Component({
  selector: 'app-update-event',
  templateUrl: './update-event.component.html',
  styleUrls: ['./update-event.component.css']
})
export class UpdateEventComponent implements OnInit {
  eventDTO: EventDTOModel;
  canEdit: boolean;
  currentEventId: string;
  eventForm: FormGroup = this.initEventForm();
  additionEventForm: FormGroup = this.initAdditionEventForm();

  eventDTOForm: FormGroup = this.formBuilder.group({
    event: this.eventForm,
    additionEvent: this.additionEventForm
  });

  constructor(private router: Router,
              private eventService: EventService,
              private alertService: AlertService,
              private formBuilder: FormBuilder,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    this.eventService.getEventById(id).subscribe((eventDTO) => {
      this.eventDTO = eventDTO;
      console.log(this.eventDTO);
    });
  }

  initEventForm(): FormGroup {
    return this.formBuilder.group({
      name: new FormControl(),
      description: new FormControl(),
      day: new FormControl(),
      startTime: new FormControl(),
      endTime: new FormControl(),
      visibility: new FormControl(),
    })
  }

  initAdditionEventForm(): FormGroup {
    return this.formBuilder.group({
      frequencyNumber: new FormControl(),
      frequencyPeriod: new FormControl(),
      priority: new FormControl(),
      people: new FormControl()
    })
  }

  update(eventDTO: EventDTOModel) {
    eventDTO.event.id = this.currentEventId;
    if (eventDTO.event.startTime != null) {
      this.eventDTO.event.startTime = (eventDTO.event.startTime).slice(0, 10) + ' ' + (eventDTO.event.startTime).slice(11, 16);
    } else if (eventDTO.event.endTime != null) {
      this.eventDTO.event.endTime = (eventDTO.event.endTime).slice(0, 10) + ' ' + (eventDTO.event.endTime).slice(11, 16);
    }
    this.eventDTO.event.name = eventDTO.event.name;
    this.eventDTO.event.description = eventDTO.event.description;
    this.eventDTO.additionEvent.priority = eventDTO.additionEvent.priority;

    console.log("before update > " + JSON.stringify(this.eventDTO));
    this.eventService.updateEvent(this.eventDTO)
      .subscribe(() => {
        this.alertService.info('Event successfully updated!', true);
        this.router.navigate(['../home']);
      });
  }

}
