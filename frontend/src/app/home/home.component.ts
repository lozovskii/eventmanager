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
    console.log('Home constructor');
  }

  ngOnInit() {
    console.log('Home on init');
    this.currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    console.log(this.currentUser);
  }

  event() {
    console.log('onSubmit');
    this.router.navigate(['events/event']);
  }

}
