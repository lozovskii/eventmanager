import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Item} from "../../_models/wishList/item";
import {WishListItem} from "../../_models/wishList/wishListItem";
import {WishList} from "../../_models/wishList/wishList";
import {WishListService} from "../../_services/wishlist.service";
import {ItemRater} from "../../_models/wishList/itemRater";

@Component({
  selector: 'app-item-details-view',
  templateUrl: './item-details-view.component.html',
  styleUrls: ['./item-details-view.component.css']
})
export class ItemDetailsViewComponent implements OnInit, OnChanges {

  @Input('isBooker') boolean = false;
  @Input('itemView') itemView: Item;
  @Input('wishListItemView') wishListItemView: WishListItem;
  @Output('updatedItem') updatedItem = new EventEmitter<WishListItem>();
  @Output('cancelledItem') cancelledItem = new EventEmitter<WishListItem>();
  @Output('bookedItem') bookedItem = new EventEmitter<WishListItem>();

  advancedMode: boolean = false;
  item: Item;
  wishListItem: WishListItem;
  customerLogin: string;
  isBooker: boolean = false;
  rated: boolean;
  rating: ItemRater;

  constructor(private wishListService: WishListService) {
    this.item = new Item();
    this.wishListItem = new WishListItem();
    this.wishListItem.item = new Item();
  }

  ngOnInit(): void {
    this.customerLogin = JSON.parse(sessionStorage.getItem('currentUser')).login;
    this.advancedMode = false;
  }

  ngOnChanges(changes: SimpleChanges) {
    this.item = new Item();
    this.wishListItem = new WishListItem();
    this.wishListItem.item = new Item();
    this.advancedMode = false;
    this.isBooker = false;
    this.rated = false;

    for (let prop in changes) {
      if (prop == 'wishListItemView') {
        this.wishListItem = changes[prop].currentValue;
        if (this.wishListItem.booker_customer_login != null)
          this.isBooker = this.checkBooker(this.wishListItem.booker_customer_login);
        this.advancedMode = true;

        if (!this.wishListItem.item.raters) {
          this.wishListItem.item.raters = [];
          this.rated = false;
        } else {
          this.checkRater();
        }
      }
      else if (prop == 'itemView') {
        this.advancedMode = false;
        this.item = changes[prop].currentValue;

        if (!this.item.raters) {
          this.item.raters = [];
          this.rated = false;
        } else {
          this.checkRater();
        }
      }
    }
  }

  cancelBooking(): void {
    this.cancelledItem.emit(this.wishListItem);
    this.isBooker = false;
  }

  updatePriority(): void {
    this.updatedItem.emit(this.wishListItem);
  }

  bookItem(): void {
    this.bookedItem.emit(this.wishListItem);
    this.isBooker = true;
  }

  checkBooker(bookerLogin: string): boolean {
    return this.wishListService.isBooker(bookerLogin);
  }

  checkRater(): void {
    this.rating = this.advancedMode ?
      this.wishListItem.item.raters.find(r => r.customer_login == this.customerLogin) :
      this.rating = this.item.raters.find(r => r.customer_login == this.customerLogin);
    this.rated = this.rating != null;
  }

  updateRating(): void {
    let value;
    let raterLogin = this.customerLogin;
    if (this.rated) {
      value = this.rating.id;
      raterLogin = null;
      let index = this.advancedMode ?
        this.wishListItem.item.raters.indexOf(this.rating) :
        this.item.raters.indexOf(this.rating);
      this.advancedMode ?
        this.wishListItem.item.raters.splice(index, 1) :
        this.item.raters.splice(index, 1);
    } else {
      let r = new ItemRater();
      r.customer_login = this.customerLogin;
      value = this.advancedMode ?
        this.wishListItem.item.id :
        this.item.id;
      this.advancedMode ?
        this.wishListItem.item.raters.push(r) :
        this.item.raters.push(r);
    }
    this.wishListService.updateRating(value, raterLogin)
      .subscribe(() => {
      }, () => {
      });
  }

}
