import { Injectable } from '@angular/core';
import {Subject} from "rxjs/Subject";

@Injectable()
export class NavbarService {

  private navStateSource = new Subject<any>();
  navState$ = this.navStateSource.asObservable();
  constructor() { }

  setNavBarState( state ) {
    this.navStateSource.next( state );
  }
}
