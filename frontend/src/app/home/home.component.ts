import {Component, OnInit} from '@angular/core';

import {User} from '../_models';
import {UserService} from '../_services';
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
        this.currentUser = user;
        localStorage.setItem('currentUserObject', JSON.stringify(this.currentUser));
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

  ngOnInit() {

  }

  event() {
    console.log('onSubmit');
    this.router.navigate(['events/event']);
  }

}
