import {Component, Input, OnInit} from '@angular/core';
import {EventDTOModel} from "../../../_models/dto/eventDTOModel";
import {EventService} from "../../../_services";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-draft-content',
  templateUrl: './draft-content.component.html',
  styleUrls: ['./draft-content.component.css']
})
export class DraftContentComponent implements OnInit {
  @Input('eventId') eventId: string;

  eventDTO: EventDTOModel;
  isCreator: boolean;

  constructor(private eventService: EventService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    this.eventId = this.activatedRoute.snapshot.paramMap.get('id');
    this.isCreator = false;
    const id = this.eventId;
    this.eventService.getEventById(id).subscribe((eventDTO : EventDTOModel) => {
      this.eventDTO = eventDTO;
      let currentUserId = JSON.parse(sessionStorage.getItem('currentUser')).id;
      this.isCreator = currentUserId == this.eventDTO.event.creatorId;
    });
  }

}
