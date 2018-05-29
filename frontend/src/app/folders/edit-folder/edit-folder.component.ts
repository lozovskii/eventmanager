import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FolderService} from "../../_services/folder.service";
import {AlertService} from "../../_services/alert.service";
import {Folder} from "../../_models/folder";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-edit-folder',
  templateUrl: './edit-folder.component.html',
  styleUrls: ['./edit-folder.component.css']
})
export class EditFolderComponent implements OnInit {
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
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20), Validators.pattern("^[ a-zA-Zа-яА-ЯієїґІЄЇҐ]*$")]]
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

  updateFolder(folder:Folder): void{
    console.log(JSON.stringify(folder));
    this.folderService.updateFolderName(folder.id, folder.name).subscribe(()=>{
      this.alertService.info('Folder name successfully updated!',true);
      this.router.navigate(['../folder-list','all']);
      });
  }

  get name() {
    return this.folderForm.get('name');
  }
}
