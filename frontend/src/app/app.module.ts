import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import { AppComponent }  from './app.component';
import { routing }        from './app.routing';

import { AlertComponent } from './_directives/index';
import { AuthGuard } from './_guards/index';
import {AlertService, AuthenticationService, RegistrationService, UserService} from './_services/index';
import { HomeComponent } from './home/index';
import { LoginComponent } from './login/index';
import { RegisterComponent } from './register/index';
import { LandingPageComponent } from './landing-page/index';
import { NavbarComponent } from './navbar/navbar.component';
import { FooterComponent } from './footer/footer.component';
import { ReCaptchaModule } from 'angular2-recaptcha';
import { EventComponent } from './event/event.component';
import {EventService} from "./_services/event.service";
import { EventlistComponent } from './eventlist/eventlist.component';
import { RegistrationConfirmComponent } from './registration-confirm/registration-confirm.component';
import { ProfileComponent } from './profile/profile.component';
import { VnavbarComponent } from './vnavbar/vnavbar.component';
import { NotelistComponent } from './notelist/notelist.component';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    routing,
    BrowserAnimationsModule,
    ReCaptchaModule,
    ReactiveFormsModule
  ],
  declarations: [
    AppComponent,
    AlertComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    LandingPageComponent,
    NavbarComponent,
    FooterComponent,
    EventComponent,
    EventlistComponent,
    RegistrationConfirmComponent
    ProfileComponent,
    VnavbarComponent,
    NotelistComponent,
  ],
  providers: [
    AuthGuard,
    AlertService,
    AuthenticationService,
    UserService,
    EventService,
    RegistrationService
    // provider used to create fake backend
  ],
  bootstrap: [AppComponent]
})

export class AppModule {}
