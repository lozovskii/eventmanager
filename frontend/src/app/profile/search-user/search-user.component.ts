import {Component, OnInit} from '@angular/core';
import {EventService, UserService} from "../../_services";
import {Event, User} from "../../_models";
import {ActivatedRoute} from "@angular/router";
import {AlertService} from "../../_services/alert.service";
import {ProfileService} from "../../_services/profile.service";

@Component({
  selector: 'app-search-user',
  templateUrl: './search-user.component.html',
  styleUrls: ['./search-user.component.css']
})

export class SearchUserComponent implements OnInit {
  users: User[];

  constructor(private profileService: ProfileService,
              private userService: UserService,
              private alertService: AlertService) {
  }

  ngOnInit() {}
/*
  searchUser(request) {
    this.profileService.search(request)
      .subscribe(
        users => {
          this.users = users;
        }
      )
  }*/

}
