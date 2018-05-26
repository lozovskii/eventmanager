import {Component, OnInit} from '@angular/core';

import {User} from '../_models';
import {UserService} from '../_services';
import {Router} from "@angular/router";

@Component({
  moduleId: module.id.toString(),
  templateUrl: 'home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {
  currentUser: User = new User();

  constructor(private userService: UserService,
              private router: Router) {
    this.currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
  }



  ngOnInit() {
    this.currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
  }

  event() {
    console.log('onSubmit');
    this.router.navigate(['events/event']);
  }

}
