import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {WishList} from '../_models/wishlist';
import {Observable} from 'rxjs';
import {UserService} from "./user.service";
import {AuthenticationService} from "./authentication.service";
import {Item} from "../_models/item";
import {BehaviorSubject} from "rxjs/Rx";
import {ItemDto} from "../_models/dto/itemDto";

@Injectable()
export class WishListService {

  private wishListUrl = 'api/wishlist';
  private wishListSource = new BehaviorSubject<WishList>(new WishList());
  currentWishList = this.wishListSource.asObservable();

  constructor(private http: HttpClient,
              private userService: UserService) {
  }

  isBooker(bookerLogin : string): boolean{
    return bookerLogin == this.userService.getCurrentLogin();
  }

  setCurrentWishList(wishList : WishList) : void {
    this.wishListSource.next(wishList);
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

  createWishList(wishList: WishList){
    const url = `${this.wishListUrl}/create`;
    return this.http.post<WishList>(url, wishList, {headers: AuthenticationService.getAuthHeader()});
  }

  deleteItems(trash: ItemDto[]){
    const url = `${this.wishListUrl}/delete`;
    return this.http.post<WishList>(url, trash, {headers: AuthenticationService.getAuthHeader()});
  }

  addItems(wishList: WishList){
    const url = `${this.wishListUrl}/add`;
    return this.http.post<WishList>(url, wishList, {headers: AuthenticationService.getAuthHeader()});
  }

  createItem(item: Item){
    const url = `api/items/create`;
    return this.http.post<Item>(url, item, {headers: AuthenticationService.getAuthHeader()});
  }
}
