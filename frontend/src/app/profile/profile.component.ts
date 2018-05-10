import {Component, OnInit} from '@angular/core';
import {User} from "../_models";
import {ProfileService} from "../_services/profile.service";
import {UserService} from "../_services";
import {AlertService} from "../_services/alert.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent implements OnInit {

  users: User[];
  request = '';
  currentUser: User;
  public isSearchUser = false;
  //
  // constructor(private profileService: ProfileService,
  //             private userService: UserService,
  //             private alertService: AlertService) {
  //   let login = this.userService.getCurrentLogin();
  //   this.profileService.getCustomer(login)
  //     .subscribe(
  //       currentUser => {
  //         this.currentUser = currentUser;
  //       }
  //     )
  // }

  constructor(private profileService: ProfileService,
              private userService: UserService,
              private alertService: AlertService) {
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


  ngOnInit() {}
  closeUsers() {
    this.isSearchUser = false;
    this.users = null;
  }

  searchUser(request) {
    // if(this.request !== '') {
    //   this.isSearchUser = true
    // }
    console.log(this.request)
    this.profileService.search(request)
      .subscribe(
        users => {
          this.users = users;
          if (users.toString() == '') {
            this.alertService.info('You have no friends', true);
          }
        }
      )
  }

   onKeyUp(event){
     this.isSearchUser = true;
    console.log(event.target.value)
    this.request = event.target.value;
    this.searchUser(this.request);

  }

  addFriend(login: string) {
    this.profileService.addFriend(login).subscribe(() => {
      this.alertService.info(`Friendship request sent`);
    });
  }
}
