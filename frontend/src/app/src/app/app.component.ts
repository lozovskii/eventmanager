import {Component, ViewChild} from '@angular/core';
import {ReCaptchaComponent} from "angular2-recaptcha";

@Component({
  moduleId: module.id.toString(),
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
}
export class RegisterComponent {
  @ViewChild(ReCaptchaComponent) captcha: ReCaptchaComponent;
}
