///<reference path="landing-page/landing-page.component.ts"/>
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/index';
import { LoginComponent } from './login/index';
import { RegisterComponent } from './register/index';
import { AuthGuard } from './_guards/index';
import {LandingPageComponent} from "./landing-page/landing-page.component";
import {EventComponent} from "./event/event.component";
import {EventlistComponent} from "./eventlist/eventlist.component";
import {RegistrationConfirmComponent} from "./registration-confirm/registration-confirm.component";
import {ProfileComponent} from "./profile/profile.component";
import {VnavbarComponent} from "./vnavbar/vnavbar.component";
import {NotelistComponent} from "./notelist/notelist.component";
import {SendLinkComponent} from "./reset-password/send-link/send-link.component";

const appRoutes: Routes = [
  { path: '', component: LandingPageComponent, pathMatch: 'full'},
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'event', component: EventComponent},
  { path: 'eventlist', component: EventlistComponent},
  {path: 'registration-confirm', component: RegistrationConfirmComponent},
  {path: 'send-link', component: SendLinkComponent},
  { path: 'profile', component: ProfileComponent},
  { path: 'vnavbar', component: VnavbarComponent},
  { path: 'notelist', component: NotelistComponent},

  // otherwise redirect to home
 { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);
