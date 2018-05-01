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
        console.log(user.name);
        this.currentUser = user;
        localStorage.setItem('currentUserObject', JSON.stringify(this.currentUser));
        console.log(this.currentUser.name);
      }
    );
  }

  ngOnInit() {

  }

  event() {
    console.log('onSubmit');
    this.router.navigate(['/event']);
  }

}
