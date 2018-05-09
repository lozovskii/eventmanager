import {Injectable} from '@angular/core';
import {NotificationItem} from "../notifications/notification-item";
import {InviteNotificationComponent} from "../notifications/invite-notification/invite-notification.component";
import {HttpClient} from "@angular/common/http";
import {UserService} from "./user.service";
import {AuthenticationService} from "./authentication.service";
import {InviteNotification} from "../_models/dto/invite-notification";
import {Observable} from 'rxjs';
import 'rxjs/add/observable/of';

import {FriendRequestNotificationComponent} from "../notifications/friend-request-notification/friend-request-notification.component";
import {FriendNotification} from "../_models/dto/friend-notification";

@Injectable()
export class NotificationService {

  constructor(private http: HttpClient,
              private userService: UserService) {
  }

  getInviteNotifications(): Observable<NotificationItem[]> {
    let customerId = this.userService.getCurrentId();
    let url = `/api/events/getInviteNotifications?customerId=${customerId}`;
    return this.http.get<InviteNotification[]>(url, {headers: AuthenticationService.getAuthHeader()}).map((notif) => {
      let items: NotificationItem[] = [];
      for (let i of notif) {
        items.push(new NotificationItem(InviteNotificationComponent, i));
      }
      return items;
    });
  }

  getFriendRequestNotifications(): Observable<NotificationItem[]> {
    let customerLogin = this.userService.getCurrentLogin();
    let url = `/api/profile/notifications?login=${customerLogin}`;
    return this.http.get<FriendNotification[]>(url, {headers: AuthenticationService.getAuthHeader()}).map((notif) => {
      let items: NotificationItem[] = [];
      for (let i of notif) {
        items.push(new NotificationItem(FriendRequestNotificationComponent, i));
      }
      return items;
    });
  }
}
