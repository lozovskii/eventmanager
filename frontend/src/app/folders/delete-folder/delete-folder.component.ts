import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FolderService} from "../../_services/folder.service";
import {Folder} from "../../_models/folder";
import {AlertService} from "../../_services/alert.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-delete-folder',
  templateUrl: './delete-folder.component.html',
  styleUrls: ['./delete-folder.component.css']
})
export class DeleteFolderComponent implements OnInit {
  folders: Folder[];
  folder:Folder;
  folderForm: FormGroup;
  constructor(private router: Router,
              private alertService: AlertService,
              private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder,
              private folderService: FolderService) { }

  ngOnInit(): void {
    this.folderForm = this.initFolderForm();
    this.getFoldersByCustId()
  }

  initFolderForm(): FormGroup {
    return this.formBuilder.group({
      id : new FormControl(),
    })
  }

  getFoldersByCustId(): void {
    this.folderService.getAllFolders()
      .subscribe((folders) => {
        this.folders = folders;
        if(folders.toString() == ''){
          this.alertService.info('No folders exist yet.',true);
        }
      });
  }

  deleteFolder(folder:Folder){
    this.folderService.deleteFolder(folder.id).subscribe(() => {
        this.alertService.info('Folder successfully deleted!', true);
        this.router.navigate(['../folder-list','all']);
      },
      error => {
        this.alertService.error('Not deleted! We working.. please try again');
      });
  }


}
