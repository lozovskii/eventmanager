import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {ResetPasswordService} from "../../_services/reset-password.service";
import {AlertService} from "../../_services";

@Component({
  selector: 'app-send-link',
  templateUrl: './send-link.component.html',
  styleUrls: ['./send-link.component.css']
})
export class SendLinkComponent implements OnInit, OnChanges {
  model: any = {};
  loading = false;

  @Input('modal') inModal;
  isModal: boolean = false;

  constructor(private alertService: AlertService,
              private resetService: ResetPasswordService) { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.isModal = changes['inModal'].currentValue;
  }

  sendLink() {
    this.loading=true;
    this.resetService.sendEmail(this.model.email)
      .subscribe(() => {
        this.alertService.success("Email with link for resetting password sent.",true);
          document.getElementById('resetPasCloseBtn').click();
        },
        (error) => {
        this.alertService.error(error.error);
      });
    this.loading=false;
  }
}
