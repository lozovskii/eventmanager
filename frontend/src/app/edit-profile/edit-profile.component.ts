import { Component, OnInit } from '@angular/core';
import {ProfileService} from "../_services/profile.service";
import {AlertService, UserService} from "../_services";
import {Router} from "@angular/router";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {User} from "../_models";

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {
  profileForm: FormGroup;
  currentUser: User;
  name = ''
  lastName = ''
  phone = ''
  login = ''
  token = ''


  constructor(private profileService: ProfileService,
              private userService: UserService,
              private alertService: AlertService,
              private router: Router,
  ) {
    let login = JSON.parse(localStorage.getItem('currentUser')).login;
    this.userService.getByLogin(login).subscribe(
      user => {
        console.log(user.name);
        this.currentUser = user;
        localStorage.setItem('currentUserObject', JSON.stringify(this.currentUser));
        console.log(this.currentUser.name);

        this.name = this.currentUser.name;
        this.lastName = this.currentUser.secondName;
        this.phone = this.currentUser.phone;
        this.login = this.currentUser.login;
        this.token = this.tok();
        console.log(this.currentUser.id);
      }
    );
  }

  ngOnInit() {

    this.profileForm = new FormGroup({
      name: new FormControl(),
      lastName: new FormControl(),
      phone: new FormControl(),
      login: new FormControl(),
      token: new FormControl(),
    });

  }

  tok(): string {
    return  this.token = JSON.parse(localStorage.getItem('currentUser')).token
  }

  updateUser(user: User): void {
    console.log(user)
    this.tok();
    this.profileService.update(user)
      .subscribe(() => {
          this.alertService.success('User updated!', true);
          setTimeout(() => this.router.navigate(["/profile"]), 5000);
        },
        (error) => {
          this.alertService.error(error.error);
        })
  }

}
