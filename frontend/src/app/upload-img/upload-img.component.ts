import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpEventType} from "@angular/common/http";
import {UserService} from "../_services";
import {User} from "../_models";
import {Router} from "@angular/router";
import {AuthenticationService} from "../_services/authentication.service";

@Component({
  selector: 'app-upload-img',
  templateUrl: './upload-img.component.html',
  styleUrls: ['./upload-img.component.css']
})
export class UploadImgComponent implements OnInit {
  // file: File;
  selectedFile: File = null;

  addCarStatus = ''
  inputText = 'Defaut text'
  currentUser: User;

  constructor(
    private http: HttpClient,
    private userService: UserService,
    private router: Router) {
    let login = JSON.parse(localStorage.getItem('currentUser')).login;
    this.userService.getByLogin(login).subscribe(
      user => {
        console.log(user.name);
        this.currentUser = user;
        localStorage.setItem('currentUserObject', JSON.stringify(this.currentUser));
        console.log(this.currentUser.name);
      }
    );
  }



  ngOnInit() {
  }

  // addCar() {
  //   this.addCarStatus = 'Car is added!'
  // }
  //
  // onKeyUp(event) {
  //   this.inputText = event.target.value
  // }

  onFileSelected(event){
    this.selectedFile = <File>event.target.files[0];
  }

  onUpload() {
    const fd = new FormData();
    fd.append('image', this.selectedFile, this.selectedFile.name);
    this.http.post('/profile/edit/upload', fd, {
      reportProgress: true,
      observe: 'events',
      headers: AuthenticationService.getAuthHeader()
    })
      .subscribe(event => {
        if (event.type === HttpEventType.UploadProgress) {
          console.log('Upload Progress:' + Math.round(event.loaded / event.total * 100 ) + '%');
        } else if (event.type === HttpEventType.Response) {
          console.log(event);
        }
      });
  }

  return

  // upload(event2){
  //   this.file = event2.target.file
  // }
}
