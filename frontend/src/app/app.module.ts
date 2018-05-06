import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {EditorModule} from '@tinymce/tinymce-angular';

import {AppComponent} from './app.component';
import {routing} from './app.routing';

import {AlertComponent} from './_directives/index';
import {AuthGuard} from './_guards/index';
import {AlertService, AuthenticationService, RegistrationService, UserService} from './_services/index';
import {HomeComponent} from './home/index';
import {LoginComponent} from './login/index';
import {RegisterComponent} from './register/index';
import {LandingPageComponent} from './landing-page/index';
import {NavbarComponent} from './navbar/navbar.component';
import {FooterComponent} from './footer/footer.component';
import {ReCaptchaModule} from 'angular2-recaptcha';
import {CreateEventComponent} from './events/create-event/create-event.component';
import {EventService} from "./_services/event.service";
import {EventlistComponent} from './events/eventlist/eventlist.component';
import {RegistrationConfirmComponent} from './registration-confirm/registration-confirm.component';
import {ProfileComponent} from './profile/profile.component';
import {VnavbarComponent} from './vnavbar/vnavbar.component';
import {SendLinkComponent} from './reset-password/send-link/send-link.component';
import {ResetPasswordService} from "./_services/reset-password.service";
import {ResetComponent} from './reset-password/reset/reset.component';
import {NavbarService} from "./_services/navbar.service";
import {EventComponent} from './events/event/event.component';
import {UpdateEventComponent} from './events/update-event/update-event.component';
import {FolderListComponent} from './folder-list/folder-list.component';
import {WishListComponent} from './wishlist/wishlist.component';
import {WishListService} from "./_services/wishlist.service";
import {BookedItemsComponent} from "./bookeditems/bookeditems.component";

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    routing,
    BrowserAnimationsModule,
    ReCaptchaModule,
    ReactiveFormsModule,
    EditorModule
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
    CreateEventComponent,
    EventlistComponent,
    RegistrationConfirmComponent,
    ProfileComponent,
    VnavbarComponent,
    SendLinkComponent,
    ResetComponent,
    EventComponent,
    UpdateEventComponent,
    FolderListComponent,
    WishListComponent,
    BookedItemsComponent
  ],
  providers: [
    AuthGuard,
    AlertService,
    AuthenticationService,
    UserService,
    EventService,
    RegistrationService,
    ResetPasswordService,
    NavbarService,
    WishListService
    // provider used to create fake backend
  ],
  bootstrap: [AppComponent]
})

export class AppModule {}
