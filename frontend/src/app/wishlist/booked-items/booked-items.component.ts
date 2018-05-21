import {Component, OnInit} from '@angular/core';
import {WishList} from "../../_models/wishList/wishList";
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/wishList/item";
import {WishListItem} from "../../_models/wishList/wishListItem";

@Component({
  selector: 'app-bookeditems',
  templateUrl: './booked-items.component.html',
  styleUrls: ['../wishlist/wishlist.component.css']
})
export class BookedItemsComponent implements OnInit {

  wishList: WishList;
  updatableWishList: WishList;
  hasChanges: boolean = false;
  items: Item[];
  wishListItemView: WishListItem;
  value : number;
  path: string[] = ['item'];
  order: number = 1;

  constructor(private wishListService: WishListService,
              private alertService: AlertService) {
    this.wishList = new WishList();
    this.updatableWishList = new WishList();
    this.updatableWishList.items = [];
    this.wishListItemView = new WishListItem();
  }

  ngOnInit() {
    this.getBookedItems();
  }

  getBookedItems(): void {
    this.wishListService.getBookedItems()
      .subscribe((wishList) => {
        this.wishList = wishList;
      }, () => {
      this.alertService.error("Items not found")});
  }

  showItemDetails(item: WishListItem): void {
    this.wishListItemView = item;
  }

  cancelBooking(wishListItem: WishListItem): void {
    wishListItem.booker_customer_login = null;
    this.updatableWishList.items.push(wishListItem);

    let index = this.wishList.items.indexOf(wishListItem);
    this.wishList.items.splice(index,1);
    this.hasChanges = true;
  }

  updatedPriority(item: WishListItem): void{
    this.updatableWishList.items.push(item);
    this.hasChanges = true;
  }

  sortItems(prop: string) {
    this.path = prop.split('.');
    this.order = this.order * (-1);
    return false;
  }

  update(): void {
    if (this.updatableWishList.items != null)
    this.wishListService.update(this.updatableWishList).subscribe(() => {
      this.alertService.success('Wish list successfully updated!');
    }, () => {
      this.alertService.error('Something wrong');
    });
  }
}
