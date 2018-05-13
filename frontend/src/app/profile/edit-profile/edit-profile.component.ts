import {Component, OnInit} from '@angular/core';
import {ProfileService} from "../../_services/profile.service";
import {AlertService, UserService} from "../../_services/index";
import {Router} from "@angular/router";
import {FormControl, FormGroup} from "@angular/forms";
import {User} from "../../_models/index";


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
    // if(sessionStorage.getItem('currentUser')==null) {
    //   let login = JSON.parse(sessionStorage.getItem('currentToken')).login;
    //   this.userService.getByLogin(login).subscribe(
    //     user => {
    //       console.log(user.name);
    //       this.currentUser = user;
    //       sessionStorage.setItem('currentUser', JSON.stringify(this.currentUser));
    //       console.log(this.currentUser.name);
    //     }
    //   );
    // } else {
    //   this.currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    // }
  }



  // constructor(private profileService: ProfileService,
  //             private userService: UserService,
  //             private alertService: AlertService,
  //             private router: Router,
  // ) {
  //   let login = JSON.parse(sessionStorage.getItem('currentUser')).login;
  //   this.userService.getByLogin(login).subscribe(
  //     user => {
  //       console.log(user.name);
  //       this.currentUser = user;
  //       localStorage.setItem('currentUserObject', JSON.stringify(this.currentUser));
  //       console.log(this.currentUser.name);
  //
  //       this.name = this.currentUser.name;
  //       this.lastName = this.currentUser.secondName;
  //       this.phone = this.currentUser.phone;
  //       this.login = this.currentUser.login;
  //       console.log(this.currentUser.id);
  //     }
  //   );
  // }

  ngOnInit() {

    this.profileForm = new FormGroup({
      name: new FormControl(),
      lastName: new FormControl(),
      phone: new FormControl(),
      login: new FormControl(),
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

}
