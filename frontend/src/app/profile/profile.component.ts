import {Component, OnInit} from '@angular/core';
import {User} from "../_models";
import {ProfileService} from "../_services/profile.service";
import {UserService} from "../_services";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent implements OnInit {

  currentUser: User;

  constructor(private profileService: ProfileService,
              private userService: UserService) {
    let login = this.userService.getCurrentLogin();
    this.profileService.getCustomer(login)
      .subscribe(
        currentUser => {
          this.currentUser = currentUser;
        }
      )
  }

  ngOnInit() {}
}
