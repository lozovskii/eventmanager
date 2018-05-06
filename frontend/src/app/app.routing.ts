///<reference path="landing-page/landing-page.component.ts"/>
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/index';
import { LoginComponent } from './login/index';
import { RegisterComponent } from './register/index';
import { AuthGuard } from './_guards/index';
import {LandingPageComponent} from "./landing-page/landing-page.component";
import {CreateEventComponent} from "./create-event/create-event.component";
import {EventlistComponent} from "./eventlist/eventlist.component";
import {RegistrationConfirmComponent} from "./registration-confirm/registration-confirm.component";
import {ProfileComponent} from "./profile/profile.component";
import {VnavbarComponent} from "./vnavbar/vnavbar.component";
import {SendLinkComponent} from "./reset-password/send-link/send-link.component";
import {ResetComponent} from "./reset-password/reset/reset.component";
import {EventComponent} from "./event/event.component";
import {UpdateEventComponent} from "./update-event/update-event.component";
import {FolderListComponent} from "./folder-list/folder-list.component";
import {WishListComponent} from "./wishlist/wishlist.component";
import {BookedItemsComponent} from "./bookeditems/bookeditems.component";

const appRoutes: Routes = [
  { path: '', component: LandingPageComponent, pathMatch: 'full'},
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'create-event', component: CreateEventComponent},
  { path: 'event/:id', component: EventComponent},
  { path: 'update-event/:id', component: UpdateEventComponent},
  { path: 'eventlist/:type', component: EventlistComponent},
  {path: 'registration-confirm', component: RegistrationConfirmComponent},
  {path: 'send-reset-link', component: SendLinkComponent},
  {path: 'reset-password', component: ResetComponent},
  { path: 'profile', component: ProfileComponent},
  { path: 'vnavbar', component: VnavbarComponent},
  { path: 'folder-list', component: FolderListComponent},
  { path: 'wishlist/:id', component: WishListComponent},
  { path: 'bookeditems', component: BookedItemsComponent},

  // otherwise redirect to home
 { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);
