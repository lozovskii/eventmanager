import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import {WishList} from '../_models/wishList/wishList';
import {Observable, throwError} from 'rxjs';
import {UserService} from "./user.service";
import {AuthenticationService} from "./authentication.service";
import {Item} from "../_models/wishList/item";
import {BehaviorSubject, Subject} from "rxjs/Rx";
import {WishListItem} from "../_models/wishList/wishListItem";
import {ExtendedTag} from "../_models/wishList/extendedTag";


@Injectable()
export class WishListService {

  private wishListUrl = 'api/wishlist';
  private itemsUrl = 'api/items';

  private wishListSource = new BehaviorSubject<WishList>(new WishList());
  wishList$ = this.wishListSource.asObservable();

  constructor(private http: HttpClient,
              private userService: UserService) {
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.');
  };

  isBooker(bookerLogin : string): boolean{
    return bookerLogin == this.userService.getCurrentLogin();
  }

  setCurrentWishList(wishList : WishList) : void {
    this.wishListSource.next(wishList);
  }

  getByEventId(eventId: string): Observable<WishList> {
    const url = `${this.wishListUrl}/${eventId}`;
    return this.http.get<WishList>(url,{headers: AuthenticationService.getAuthHeader()})
      .pipe(
        catchError(this.handleError));
  }

  getBookedItems(): Observable<WishList> {
    let bookerLogin = this.userService.getCurrentLogin();
    const url = `${this.wishListUrl}/booked?customerLogin=${bookerLogin}`;
    return this.http.get<WishList>(url,{headers: AuthenticationService.getAuthHeader()})
      .pipe(
        catchError(this.handleError));
  }

  update(wishList: WishList){
    const url = `${this.wishListUrl}/update`;
    return this.http.post<WishList>(url, wishList, {headers: AuthenticationService.getAuthHeader()});
  }

  removeItems(trash: WishListItem[]){
    const url = `${this.wishListUrl}/delete`;
    return this.http.post<WishList>(url, trash, {headers: AuthenticationService.getAuthHeader()});
  }

  addItems(wishList: WishList){
    const url = `${this.wishListUrl}/add`;
    return this.http.post<WishList>(url, wishList, {headers: AuthenticationService.getAuthHeader()});
  }

  createItem(item: Item){
    const url = `${this.itemsUrl}/create`;
    return this.http.post<Item>(url, item, {headers: AuthenticationService.getAuthHeader()});
  }

  copyItems(items: Item[]){
    const url = `${this.itemsUrl}/batch-create`;
    return this.http.post<Item>(url, items, {headers: AuthenticationService.getAuthHeader()});
  }

  deleteItems(trash: Item[]){
    const url = `${this.itemsUrl}/batch-delete`;
    return this.http.post<Item>(url, trash, {headers: AuthenticationService.getAuthHeader()});
  }

  deleteTags(trash: ExtendedTag[]){
    const url = `${this.itemsUrl}/batch-delete-tags`;
    return this.http.post<Item>(url, trash, {headers: AuthenticationService.getAuthHeader()});
  }

  updateItem(item: Item){
    const url = `${this.itemsUrl}/update`;
    return this.http.put<Item>(url, item, {headers: AuthenticationService.getAuthHeader()});
  }

  updateRating(itemId: string, customerLogin:string) {
    const url = `${this.itemsUrl}/update-rating`;
    let params = [itemId, customerLogin];
    return this.http.put(url,params, {headers: AuthenticationService.getAuthHeader()});
  }

  getItemsCollection(): Observable<Item[]> {
    let currentLogin = this.userService.getCurrentLogin();
    const url = `${this.itemsUrl}/collection?customerLogin=${currentLogin}`;
    return this.http.get<Item[]>(url,{headers: AuthenticationService.getAuthHeader()})
      .pipe(
        catchError(this.handleError));
  }

  getAllItems(): Observable<Item[]> {
    const url = `${this.itemsUrl}/all`;
    return this.http.get<Item[]>(url,{headers: AuthenticationService.getAuthHeader()})
      .pipe(
        catchError(this.handleError));
  }

  getPopularItems(): Observable<Item[]> {
    const url = `${this.itemsUrl}/popular`;
    return this.http.get<Item[]>(url,{headers: AuthenticationService.getAuthHeader()})
      .pipe(
        catchError(this.handleError));
  }

  searchItems(search: string) {
    console.log('In woshlistService/ query: ' + search )
    const url = `${this.itemsUrl}/search?search=${search}`;
    console.log(url);
    return this.http.get<Item[]>(url,{headers: AuthenticationService.getAuthHeader()})
      .pipe(
        catchError(this.handleError));
  }


  getPageAllItems(page: number, size: number) {
    const url = `${this.itemsUrl}/pageAll`;
    return this.http.get(url, {headers: AuthenticationService.getAuthHeader()});
  }
}
