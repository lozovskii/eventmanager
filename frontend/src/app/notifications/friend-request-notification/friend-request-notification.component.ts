import {Component, Input} from '@angular/core';
import {NotificationComponent} from "../notification.component";
import {FriendNotification} from "../../_models/dto/friend-notification";
import {ProfileService} from "../../_services/profile.service";
import {AlertService} from "../../_services";

@Component({
  selector: 'app-friend-request-notification',
  templateUrl: 'friend-request-notification.component.html',
  styleUrls: ['./friend-request-notification.component.css']
})
export class FriendRequestNotificationComponent implements NotificationComponent {
  @Input() data: FriendNotification;
  @Input() ref;

  constructor(private profileService: ProfileService,
              private alertService: AlertService) {

  }

  acceptRequest() {
    this.profileService.acceptFriend(this.data.id).subscribe(() => {
      this.alertService.info(`You successfully accepted friendship with ${this.data.name} ${this.data.second_name}`);
      this.ref.destroy();
    }, () => {
      this.alertService.error('Something went wrong.');
    });
  }

  declineRequest() {
    this.profileService.rejectFriend(this.data.id).subscribe(() => {
      this.alertService.info(`You successfully rejected friendship with ${this.data.name} ${this.data.second_name}`);
      this.ref.destroy();
    }, () => {
      this.alertService.error('Something went wrong.');
    });
  }
}
