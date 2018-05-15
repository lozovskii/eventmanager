import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Folder} from "../../_models/folder";
import {ActivatedRoute, Router} from "@angular/router";
import {FolderService} from "../../_services/folder.service";
import {AlertService} from "../../_services";

@Component({
  selector: 'app-move-note',
  templateUrl: './move-note.component.html',
  styleUrls: ['./move-note.component.css']
})
export class MoveNoteComponent implements OnInit {
  @Input('noteId') noteId: string;

  moveNoteForm:FormGroup;
  event:Event;
  folder:Folder;
  constructor(private formBuilder: FormBuilder,
              private activatedRoute: ActivatedRoute,
              private alertService: AlertService,
              private folderService: FolderService,
              private router: Router) { }

  ngOnInit() {
    this.noteId = this.activatedRoute.snapshot.paramMap.get('id');
    console.log('noteId = ' + this.noteId);
    this.moveNoteForm = this.initMoveNoteForm();
  }

  initMoveNoteForm(): FormGroup {
    return this.formBuilder.group({
      name : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]]
    })
  }

  moveNote(folder:Folder){
    console.log(folder);
    this.folderService.moveNote(this.noteId,folder.name).subscribe(()=>{
      this.alertService.info('Note successfully moved!',true);
      this.router.navigate(['/folder-list']);
    });
  }

}
