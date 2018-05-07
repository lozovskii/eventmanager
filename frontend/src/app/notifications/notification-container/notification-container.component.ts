import {Component, ComponentFactoryResolver, OnInit, ViewChild} from '@angular/core';
import {NotificationItem} from "../notification-item";
import {NotificationsHostDirective} from "../notifications-host.directive";
import {NotificationComponent} from "../notification.component";
import {NotificationService} from "../../_services/notification.service";

@Component({
  selector: 'app-notification-container',
  styleUrls: ['./notification-container.component.css'],
  templateUrl: 'notification-container.component.html'
})
export class NotificationContainerComponent implements OnInit {
  notifications: NotificationItem[];
  @ViewChild(NotificationsHostDirective) notificationHost: NotificationsHostDirective;

  constructor(private componentFactoryResolver: ComponentFactoryResolver,
              private notificationService: NotificationService) {
  }

  ngOnInit() {
    this.loadComponents();
  }

  loadComponents() {
    this.notificationService.getInviteNotifications().subscribe((notifications => this.displayNotifications(notifications)));
    this.notificationService.getFriendRequestNotifications().subscribe((notifications => this.displayNotifications(notifications)));

  }

  displayNotifications(notifications: NotificationItem[]) {
    notifications.forEach((notification) => {
      let componentFactory = this.componentFactoryResolver.resolveComponentFactory(notification.component);
      let viewContainerRef = this.notificationHost.viewContainerRef;

      let componentRef = viewContainerRef.createComponent(componentFactory);
      (<NotificationComponent>componentRef.instance).data = notification.data;
      (<NotificationComponent>componentRef.instance).ref = componentRef;
    })
  }

}
