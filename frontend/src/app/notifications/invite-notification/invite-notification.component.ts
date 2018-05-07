import {Component, Input} from '@angular/core';
import {NotificationComponent} from "../notification.component";
import {AlertService, EventService} from "../../_services";
import {InviteNotification} from "../../_models/dto/invite-notification";

@Component({
  selector: 'app-invite-notification',
  styleUrls: ['./invite-notification.component.css'],

  templateUrl: 'invite-notification.component.html'
})

export class InviteNotificationComponent implements NotificationComponent {
  @Input() data: InviteNotification;
  @Input() ref;

  constructor(private eventService: EventService,
              private alertService: AlertService) {

  }

  acceptInvite() {
    this.eventService.addParticipant(this.data.eventId).subscribe(() => {
      this.alertService.success(`You successfully joined event '${this.data.eventName}'`);
      this.ref.destroy();
    }, () => {
      this.alertService.error('Something went wrong.');
    });
  }

  declineInvite() {
    this.eventService.removeParticipant(this.data.eventId).subscribe(() => {
      this.alertService.success(`You successfully left event '${this.data.eventName}'`);
      this.ref.destroy();
    }, () => {
      this.alertService.error('Something went wrong.');
    });

  }
}
