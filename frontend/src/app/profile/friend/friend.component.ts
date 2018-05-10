import {Component, OnInit} from '@angular/core';
import {ProfileService} from "../../_services/profile.service";
import {AlertService, UserService} from "../../_services/index";
import {User} from "../../_models/index";

@Component({
  selector: 'app-friends',
  templateUrl: './friend.component.html',
  styleUrls: ['./friend.component.css']
})

export class FriendComponent implements OnInit {

  friends: User[];

  constructor(private profileService: ProfileService,
              private userService: UserService,
              private alertService: AlertService) {
    let login = this.userService.getCurrentLogin();
    this.profileService.getFriends(login)
      .subscribe(
        friends => {
          this.friends = friends;
          if (friends.toString() == '') {
            this.alertService.info('You have no friends', true);
          }
        }
      )
  }

  deleteFriend(login: string, name: string, secondName: string) {
    this.profileService.deleteFriend(login).subscribe(() => {
      this.alertService.info(`${name} ${secondName} was removed from your friends list`);
      location.reload(true);
    });
  }

  ngOnInit() {}
}
