import {Component, OnInit} from '@angular/core';
import {User} from "../_models/index";
import {AlertService} from "../_services/alert.service";
import {FriendsService} from "../_services/friends.service";

@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.css']
})

export class FriendsComponent implements OnInit {

  friends: User[];
  login: string;

  constructor(private friendsService: FriendsService,
              private alertService: AlertService) {
    this.login = JSON.parse(sessionStorage.getItem('currentUser')).login;
  }

  ngOnInit() {
    // this.friendsService.getFriends()
    //   .subscribe((friends) => {
    //     this.friends = friends;
    //     console.log(this.friends);
    //     if(friends.toString() == ''){
    //       this.alertService.info('You have no friends',true);
    //     }
    //   });
// getFriends(): void {
        this.friendsService.getFriends(this.login)
          .subscribe((friends) => {
            this.friends = friends;

            if(friends.toString() == '') {
              this.alertService.info('You have no friends',true);
            }
          });
      // }
  }


}
