import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Folder} from "../../_models/folder";
import {AlertService, UserService} from "../../_services";
import {FolderService} from "../../_services/folder.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-folder',
  templateUrl: './create-folder.component.html',
  styleUrls: ['./create-folder.component.css']
})
export class CreateFolderComponent implements OnInit {
  folderForm: FormGroup = this.initFolderForm();
  customerId:string;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private userService: UserService,
              private folderService: FolderService,
              private alertService: AlertService) { }

  ngOnInit() {
    this.customerId = this.userService.getCurrentId();
  }

  initFolderForm(): FormGroup {
    return this.formBuilder.group({
      name : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      customerId:new FormControl(''),
      isshared: new FormControl('')
    })
  }

  createFolder(folder:Folder) {
    console.log(JSON.stringify(folder));
    folder.customerId = this.customerId;
    this.folderService.create(folder).subscribe(data => {
        this.alertService.info('Folder successfully saved!', true);
        this.router.navigate(['../folder-list','all']);
      },
      error => {
        this.alertService.error('Not saved! We working.. please try again');
      });
  }
}
