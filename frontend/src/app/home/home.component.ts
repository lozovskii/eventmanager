import { Component, OnInit } from '@angular/core';

import { User } from '../_models/user';
import { UserService } from '../_services/index';
import {Router} from "@angular/router";

@Component({
  moduleId: module.id.toString(),
  templateUrl: 'home.component.html'
})

export class HomeComponent implements OnInit {
  currentUser: User;

  constructor(private userService: UserService,
              private router: Router) {
    let login = JSON.parse(localStorage.getItem('currentUser')).login;
    this.userService.getByLogin(login).subscribe(
      user => {
        console.log(user.name);
        this.currentUser= user;
        localStorage.setItem('currentUserObject', JSON.stringify(this.currentUser));
        console.log(this.currentUser.name);
      }
    );}

  ngOnInit() {

  }

//   deleteUser(id: number) {
//     this.userService.delete(id).subscribe(() => { this.loadAllUsers() });
//   }
//
//   private loadAllUsers() {
//     this.userService.getAll().subscribe(users => { this.users = users; });
//   }

  event() {
    console.log('onSubmit');
    this.router.navigate(['/event']);
  }

 }
