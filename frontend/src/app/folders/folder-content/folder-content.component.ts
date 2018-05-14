import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AlertService} from "../../_services/alert.service";
import {Event} from "../../_models";
import {FolderService} from "../../_services/folder.service";
import {EventService, UserService} from "../../_services";

@Component({
  selector: 'app-folder-content',
  templateUrl: './folder-content.component.html',
  styleUrls: ['./folder-content.component.css']
})
export class FolderContentComponent implements OnInit {
  @Input('folderId') folderId: string;

  isTitle: boolean;
  events: Event[];
  constructor(private folderService: FolderService,
              private activatedRoute: ActivatedRoute,
              private alertService: AlertService,
              private userService: UserService,
              private eventService: EventService) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      let type = params['type'];
      switch (type) {
        case 'default' : {
          this.getNotesByCustId();
          break;
        }
        case 'folder' : {
          this.folderId = this.activatedRoute.snapshot.paramMap.get('folderId');
          this.getNotesByCustIdFolderid();
          break;
        }
      }
    });
  }

  getNotesByCustIdFolderid(): void {
    let custId = this.userService.getCurrentId();
    this.folderService.getNotesByCustIdFolderId(custId, this.folderId)
      .subscribe((events) => {
        this.isTitle = true;
        this.events = events;
        if(events.toString() == ''){
          this.isTitle = false;
          this.alertService.info('This folder is empty.',true);
        }
      });
  }

  getNotesByCustId(): void {
    this.eventService.getNotesByCustId()
      .subscribe((events) => {
        this.isTitle = true;
        this.events = events;
        console.log(this.events);
        if(events.toString() == ''){
          this.isTitle = false;
          this.alertService.info('You have no notes yet.',true);
        }
      });
  }

}
