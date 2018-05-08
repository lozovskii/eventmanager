import { Component, OnInit } from '@angular/core';
// Base 64 IMage display issues with unsafe image
import { DomSanitizer } from '@angular/platform-browser';
import {User} from "../_models";
import {AlertService, UserService} from "../_services";
import {ProfileService} from "../_services/profile.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-upload-img',
  templateUrl: './upload-img.component.html',
  styleUrls: ['./upload-img.component.css']
})
export class UploadImgComponent implements OnInit {

  private base64Image: string;

  currentUser: User;

  constructor(private domSanitizer: DomSanitizer,
              private profileService: ProfileService,
              private userService: UserService,
              private alertService: AlertService,
              private router: Router,
  ) {
    let login = JSON.parse(localStorage.getItem('currentUser')).login;
    this.userService.getByLogin(login).subscribe(
      user => {
        this.currentUser = user;
        localStorage.setItem('currentUserObject', JSON.stringify(this.currentUser));

      }
    );
  }


  ngOnInit() {
  }

  changeListener($event): void {
    this.readThis($event.target);
  }

  readThis(inputValue: any): void {
    var file: File = inputValue.files[0];
    var myReader: FileReader = new FileReader();

    myReader.onloadend = (e) => {
      this.base64Image = myReader.result;

    }
   myReader.readAsDataURL(file);
  }

  updateUser(user: User): void {
    console.log(JSON.stringify(user))
    user.avatar = this.base64Image;
    console.log('Update user' + user.avatar)
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
