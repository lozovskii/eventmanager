import { Component, OnInit } from '@angular/core';
// Base 64 IMage display issues with unsafe image
import { DomSanitizer } from '@angular/platform-browser';
import {User} from "../_models";
import {AlertService, UserService} from "../_services";
import {ProfileService} from "../_services/profile.service";
import {Router} from "@angular/router";
import {MAX_IMG_SIZE} from "../app.config";
import {ALLOWED_IMG_FORMATS} from "../app.config";


@Component({
  selector: 'app-upload-img',
  templateUrl: './upload-img.component.html',
  styleUrls: ['./upload-img.component.css']
})
export class UploadImgComponent implements OnInit {

  allowedFormats: string[] =  ALLOWED_IMG_FORMATS;
  maxImageSize = MAX_IMG_SIZE;
  base64Image: string;
  currentUser: User;
  loading = true;



  constructor(private domSanitizer: DomSanitizer,
              private profileService: ProfileService,
              private userService: UserService,
              private alertService: AlertService,
              private router: Router,
  ) {
    // let login = JSON.parse(localStorage.getItem('currentUser')).login;
    // this.userService.getByLogin(login).subscribe(
    //   user => {
    //     this.currentUser = user;
    //     localStorage.setItem('currentUserObject', JSON.stringify(this.currentUser));
    //   }
    // );
    if(sessionStorage.getItem('currentUser')==null) {
      let login = JSON.parse(sessionStorage.getItem('currentToken')).login;
      this.userService.getByLogin(login).subscribe(
        user => {
          console.log(user.name);
          this.currentUser = user;
          sessionStorage.setItem('currentUser', JSON.stringify(this.currentUser));
          console.log(this.currentUser.name);
        }
      );
    } else {
      this.currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    }
  }


  ngOnInit() {
  }

  changeListener($event): void {
    this.readThis($event.target);
  }


  getFileExtension(filename) {
    return filename.split('.').pop();
  }


  validFormat(format: string) {
    let isValid = false;
    for (let allowedFormat of this.allowedFormats) {
      if (allowedFormat == format) {
        isValid = true;
      }
    }
    return isValid;
  }


  validSize(file) {
    if(file.size<= this.maxImageSize) {
      return true;
    }else return false;
  }


  validImg(file: File){
    let format = this.getFileExtension(file.name);
    console.log(file.size)
    if(this.validFormat(format) && this.validSize(file)){
      this.loading = false;
    } else this.loading = true;
  }


  readThis(inputValue: any): void {
    var file: File = inputValue.files[0];
    var myReader: FileReader = new FileReader();
    myReader.onloadend = (e) => {
      this.base64Image = myReader.result;
      this.validImg(file)
    }
   myReader.readAsDataURL(file);
  }

  updateUser(user: User): void {
    // console.log(JSON.stringify(user))
    user.avatar = this.base64Image;
    this.profileService.update(user)
      .subscribe(() => {
          this.alertService.success('User updated!', true);
          setTimeout(() => this.router.navigate(["/profile"]), 500);
        },
        (error) => {
          this.alertService.error(error.message);
        })
  }

}
