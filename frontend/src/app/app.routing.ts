///<reference path="landing-page/landing-page.component.ts"/>
import {RouterModule, Routes} from '@angular/router';

import {HomeComponent} from './home/index';
import {LoginComponent} from './login/index';
import {RegisterComponent} from './register/index';
import {AuthGuard} from './_guards/index';
import {LandingPageComponent} from "./landing-page/landing-page.component";
import {CreateEventComponent} from "./events/create-event/create-event.component";
import {EventlistComponent} from "./events/eventlist/eventlist.component";
import {RegistrationConfirmComponent} from "./registration-confirm/registration-confirm.component";
import {ProfileComponent} from "./profile/profile.component";
import {VnavbarComponent} from "./vnavbar/vnavbar.component";
import {SendLinkComponent} from "./reset-password/send-link/send-link.component";
import {ResetComponent} from "./reset-password/reset/reset.component";
import {EventComponent} from "./events/event/event.component";
import {UpdateEventComponent} from "./events/update-event/update-event.component";
import {FolderListComponent} from "./folders/folder-list/folder-list.component";
import {WishListComponent} from "./wishlist/wishlist.component";
import {EditProfileComponent} from "./profile/edit-profile/edit-profile.component";
import {UploadImgComponent} from "./upload-img/upload-img.component";
import {NotificationContainerComponent} from "./notifications/notification-container/notification-container.component";
import {BookedItemsComponent} from "./wishlist/bookeditems/bookeditems.component";
import {FriendComponent} from "./profile/friend/friend.component";
import {CreateItemComponent} from "./wishlist/create-item/create-item.component";
import {CreatedItemsComponent} from "./wishlist/createdItems/createditems.component";
import {EditWishListComponent} from "./wishlist/edit-wishlist/edit-wishlist.component";
import {CreateFolderComponent} from "./folders/create-folder/create-folder.component";
import {EventContainerComponent} from "./events/event-container/event-container.component";
import {FolderContentComponent} from "./folders/folder-content/folder-content.component";

const appRoutes: Routes = [
  { path: '', component: LandingPageComponent, pathMatch: 'full'},
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'create-event', component: CreateEventComponent, canActivate: [AuthGuard]},
  { path: 'event/:id', component: EventComponent, canActivate: [AuthGuard]},
  { path: 'update-event/:id', component: UpdateEventComponent, canActivate: [AuthGuard]},
  { path: 'eventlist/:type', component: EventlistComponent, canActivate: [AuthGuard]},
  { path: 'registration-confirm', component: RegistrationConfirmComponent},
  { path: 'send-reset-link', component: SendLinkComponent},
  { path: 'reset-password', component: ResetComponent},
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  { path: 'vnavbar', component: VnavbarComponent, canActivate: [AuthGuard]},
  { path: 'folder-list/:type', component: FolderListComponent, canActivate: [AuthGuard]},
  { path: 'wishlist', component: WishListComponent, canActivate: [AuthGuard]},
  { path: 'edit-profile', component: EditProfileComponent, canActivate: [AuthGuard]},
  { path: 'upload-img', component: UploadImgComponent, canActivate: [AuthGuard]},
  { path: 'notifications', component: NotificationContainerComponent, canActivate: [AuthGuard]},
  { path: 'profile/friends', component: FriendComponent, canActivate: [AuthGuard]},
  { path: 'booked-items', component: BookedItemsComponent, canActivate: [AuthGuard]},
  { path: 'create-item', component: CreateItemComponent, canActivate: [AuthGuard]},
  { path: 'created-items', component: CreatedItemsComponent, canActivate: [AuthGuard]},
  { path: 'wishlist/edit', component: EditWishListComponent, canActivate: [AuthGuard]},
  { path: 'create-folder', component: CreateFolderComponent, canActivate: [AuthGuard]},
  { path: 'folder-content/:folderId', component: FolderContentComponent, canActivate: [AuthGuard]},
  { path: 'event-container/:id', component: EventContainerComponent, canActivate: [AuthGuard]},
  // otherwise redirect to home
 { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);
