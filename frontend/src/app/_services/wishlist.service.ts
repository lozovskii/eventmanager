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

  isBooker(bookerId : string): boolean{
    return bookerId == this.userService.getCurrentId();
  }

  getByEventId(eventId: string): Observable<WishList> {
    const url = `${this.wishListUrl}/${eventId}`;
    return this.http.get<WishList>(url,{headers: AuthenticationService.getAuthHeader()});
  }

  getBookedItems(): Observable<WishList> {
    let customerId = this.userService.getCurrentId();
    const url = `${this.wishListUrl}/booked?customerId=${customerId}`;
    return this.http.get<WishList>(url,{headers: AuthenticationService.getAuthHeader()});
  }

  update(wishList: WishList){
    const url = `${this.wishListUrl}/update`;
    return this.http.post<WishList>(url, wishList, {headers: AuthenticationService.getAuthHeader()});
  }
}
