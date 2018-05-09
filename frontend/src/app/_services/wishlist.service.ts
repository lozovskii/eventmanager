import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {WishList} from '../_models/wishlist';
import {Observable} from 'rxjs';
import {UserService} from "./user.service";
import {AuthenticationService} from "./authentication.service";
import {Item} from "../_models/item";

@Injectable()
export class WishListService {
  private wishListUrl = 'api/wishlist';

  constructor(private http: HttpClient,
              private userService: UserService) {
  }

  isBooker(bookerLogin : string): boolean{
    return bookerLogin == this.userService.getCurrentLogin();
  }

  getByEventId(eventId: string): Observable<WishList> {
    const url = `${this.wishListUrl}/${eventId}`;
    return this.http.get<WishList>(url,{headers: AuthenticationService.getAuthHeader()});
  }

  getBookedItems(): Observable<WishList> {
    let bookerLogin = this.userService.getCurrentLogin();
    const url = `${this.wishListUrl}/booked?customerLogin=${bookerLogin}`;
    return this.http.get<WishList>(url,{headers: AuthenticationService.getAuthHeader()});
  }

  getCreatedItems(): Observable<Item[]> {
    let currentLogin = this.userService.getCurrentLogin();
    const url = `api/items/created?customerLogin=${currentLogin}`;
    return this.http.get<Item[]>(url,{headers: AuthenticationService.getAuthHeader()});
  }

  update(wishList: WishList){
    const url = `${this.wishListUrl}/update`;
    return this.http.post<WishList>(url, wishList, {headers: AuthenticationService.getAuthHeader()});
  }

  create(wishList: WishList){
    const url = `${this.wishListUrl}/create`;
    return this.http.post<WishList>(url, wishList, {headers: AuthenticationService.getAuthHeader()});
  }
}
