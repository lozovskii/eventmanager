import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

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
import {FolderListComponent} from './folders/folder-list/folder-list.component';
import {WishListComponent} from './wishlist/wishlist.component';
import {WishListService} from "./_services/wishlist.service";
import {BookedItemsComponent} from "./wishlist/bookeditems/bookeditems.component";
import {EditProfileComponent} from './profile/edit-profile/edit-profile.component';
import {ProfileService} from "./_services/profile.service";
import {UploadImgComponent} from './upload-img/upload-img.component';
import {NotificationsHostDirective} from './notifications/notifications-host.directive';
import {InviteNotificationComponent} from './notifications/invite-notification/invite-notification.component';
import {NotificationContainerComponent} from './notifications/notification-container/notification-container.component';
import {NotificationService} from "./_services/notification.service";
import {FriendRequestNotificationComponent} from './notifications/friend-request-notification/friend-request-notification.component';
import {CreateItemComponent} from "./wishlist/create-item/create-item.component";
import {CreatedItemsComponent} from "./wishlist/createdItems/createditems.component";
import {FriendComponent} from "./profile/friend/friend.component";
import {SortingItemsPipe} from "./wishlist/sorting-items/sorting-items.pipe";
import {SearchUserComponent} from "./profile/search-user/search-user.component";
import { EventNotificationComponent } from './events/event-notification/event-notification.component';
import { CreateFolderComponent } from './folders/create-folder/create-folder.component';
import {EditWishListComponent} from "./wishlist/edit-wishlist/edit-wishlist.component";
import {CalendarComponent} from "./home/calendar/calendar.component";

import { CalendarModule } from 'angular-calendar';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import {HttpClientModule} from "@angular/common/http";
import {EventContainerComponent} from "./events/event-container/event-container.component";
@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    routing,
    BrowserAnimationsModule,
    ReCaptchaModule,
    ReactiveFormsModule,
    EditorModule,
    BrowserAnimationsModule, CalendarModule.forRoot(),
    NgbModalModule.forRoot()
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
    EditProfileComponent,
    UploadImgComponent,
    WishListComponent,
    BookedItemsComponent,
    NotificationsHostDirective,
    InviteNotificationComponent,
    NotificationContainerComponent,
    FriendRequestNotificationComponent,
    CreateItemComponent,
    CreatedItemsComponent,
    FriendRequestNotificationComponent,
    FriendComponent,
    SearchUserComponent,
    SortingItemsPipe,
    EventNotificationComponent,
    CreateFolderComponent,
    EditWishListComponent,
    EventNotificationComponent,
    CalendarComponent,
    EventContainerComponent,
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
    WishListService,
    ProfileService,
    NotificationService
    // provider used to create fake backend
  ],
  entryComponents: [
    InviteNotificationComponent,
    FriendRequestNotificationComponent
  ],
  bootstrap: [AppComponent]
})

export class AppModule {}
