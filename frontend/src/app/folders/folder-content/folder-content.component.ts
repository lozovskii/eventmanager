import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AlertService} from "../../_services/alert.service";
import {Event} from "../../_models";
import {FolderService} from "../../_services/folder.service";
import {UserService} from "../../_services";

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
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.folderId = this.activatedRoute.snapshot.paramMap.get('folderId');
    console.log('folderId = '+ this.folderId);
    this.getNotesByCustIdFolderid()
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

}
