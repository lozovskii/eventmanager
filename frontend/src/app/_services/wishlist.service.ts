import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {WishList} from '../_models/wishlist';
import {Observable} from 'rxjs/Observable';
import {UserService} from "./user.service";
import {AuthenticationService} from "./authentication.service";

@Injectable()
export class WishListService {
  private wishListUrl = 'api/wishlist';

  constructor(private http: HttpClient,
              private userService: UserService) {
  }

  getByEventId(eventId: string): Observable<WishList> {
    const url = `${this.wishListUrl}/show-wishlist${eventId}`;
    return this.http.get<WishList>(url, {headers: AuthenticationService.getAuthHeader()});
  }

  isParticipant(customerId: string, eventId: string) {
    const url = `${this.wishListUrl}/isParticipant?customerId=${customerId}&eventId=${eventId}`;
    return this.http.get(url, {headers: AuthenticationService.getAuthHeader()});
  }
}
