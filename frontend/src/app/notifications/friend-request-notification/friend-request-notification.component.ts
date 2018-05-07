import {Component, Input} from '@angular/core';
import {NotificationComponent} from "../notification.component";

@Component({
  selector: 'app-friend-request-notification',
  templateUrl: 'friend-request-notification.component.html',
  styleUrls: ['./friend-request-notification.component.css']
})
export class FriendRequestNotificationComponent implements NotificationComponent {
  @Input() data: any;
  @Input() ref;

  acceptRequest() {

  }

  declineRequest() {

  }
}
