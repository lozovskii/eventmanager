import { Component, OnInit } from '@angular/core';
import {Folder} from "../../_models/folder";
import {AlertService} from "../../_services/alert.service";
import {FolderService} from "../../_services/folder.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-folder-list',
  templateUrl: './folder-list.component.html',
  styleUrls: ['./folder-list.component.css']
})
export class FolderListComponent implements OnInit {
  folders: Folder[];
  constructor(private alertService: AlertService,
              private activatedRoute: ActivatedRoute,
              private folderService: FolderService) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      let type = params['type'];
      switch (type) {
        case 'all' : {
          this.getFoldersByCustId();
          break;
        }
      }
    });
  }

  getFoldersByCustId(): void {
    this.folderService.getAllFolders()
      .subscribe((folders) => {
        this.folders = folders;
        if(folders.toString() == ''){
          this.alertService.info('No folders exist yet.');
        }
      });
  }
}
