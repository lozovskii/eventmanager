import {Component, OnInit} from '@angular/core';
import {ProfileService} from "../../_services/profile.service";
import {AlertService, UserService} from "../../_services/index";
import {Router} from "@angular/router";
import {FormControl, FormGroup, NgForm, Validators} from "@angular/forms";
import {User} from "../../_models/index";


@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {
  profileForm: FormGroup;
  currentUser: User;
  name = '';
  lastName = '';
  phone = '';
  isDelete = false;

    constructor(private profileService: ProfileService,
              private userService: UserService,
              private alertService: AlertService,
              private router: Router,) {
    let login = JSON.parse(sessionStorage.getItem('currentUser')).login;
    this.userService.getByLogin(login).subscribe(
      user => {
        this.currentUser = user;
        sessionStorage.setItem('currentUserObject', JSON.stringify(this.currentUser));
      }
    );
  }


  ngOnInit() {

    this.profileForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.minLength(3),
        Validators.maxLength(40)]),
      lastName: new FormControl('', [Validators.required, Validators.minLength(3),
        Validators.maxLength(40)]),
      phone: new FormControl('', [ Validators.maxLength(20),
        Validators.pattern("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")]
        )
    });

  }

  updateUser(user: User): void {
    this.profileService.update(user)
      .subscribe(() => {
          this.alertService.success('User updated!', true);
          sessionStorage.setItem('currentUser', JSON.stringify(user));
          setTimeout(() => this.router.navigate(["/profile"]), 500);
        },
        (error) => {
          this.alertService.error(error.message);
        })
  }


  checkForLength(control: FormControl) {
    if(control.value.length <= 2) {
      return {
        'minLengthError': true
      };
    }
    if(control.value.length >= 19) {
      return {
        'maxLengthError': true
      };
    }
  }

  checkForPhoneNumber(control: FormControl) {
    if(control.value.length <= 3) {
      return {
        'minPhoneLengthError': true
      };
    }
      if(control.value.length >= 13) {
      return {
        'maxPhoneLengthError': true
      };
    }
    return null;
  }

  deleteAvatar(user : User) {
      user.avatar = '';
    this.updateUser(user);
  }

  isDeleteChange() {
      if (this.isDelete == false){
        this.isDelete = true;
      } else {
        this.isDelete = false;
      }

  }



}
