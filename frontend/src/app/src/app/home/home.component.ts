import { Component, OnInit } from '@angular/core';

import { User } from '../_models/user';
import { UserService } from '../_services/index';

@Component({
  moduleId: module.id.toString(),
  templateUrl: 'home.component.html'
})

export class HomeComponent implements OnInit {
  currentUser: User;
//  users: User[] = [];

  constructor(private userService: UserService) {
    let login = JSON.parse(localStorage.getItem('currentUser')).login;
    this.userService.getByLogin(login).subscribe(
      user => {
        console.log(user.name);
        this.currentUser= user;
        console.log(this.currentUser.name);
      }
    );
    // console.log("Current user " +this.currentUser);
    // console.log("Current user login " +this.currentUser.login);
  //   console.log("Current user name "+this.currentUser.name);
  }

  ngOnInit() {
    // this.loadAllUsers();
  }

//   deleteUser(id: number) {
//     this.userService.delete(id).subscribe(() => { this.loadAllUsers() });
//   }
//
//   private loadAllUsers() {
//     this.userService.getAll().subscribe(users => { this.users = users; });
//   }
 }
