import {Component, OnInit} from '@angular/core';

import {User} from '../_models';
import {UserService} from '../_services';
import {Router} from "@angular/router";
import {EventService} from "../_services";

@Component({
  moduleId: module.id.toString(),
  templateUrl: 'home.component.html'
})

export class HomeComponent implements OnInit {
  currentUser: User;

  constructor(private userService: UserService,
              private router: Router,
              private eventService: EventService) {
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
    this.eventService.getEventsByCustId().subscribe(

      events => {

        localStorage.setItem('Event', JSON.stringify(events));
        console.log(events);
        console.log(localStorage.getItem('Event'));

      }

    )
  }

  ngOnInit() {

  }


  event() {
    console.log('onSubmit');
    this.router.navigate(['events/event']);
  }

}
