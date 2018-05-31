import {Component, OnInit} from '@angular/core';
import {WishList} from "../../_models/wishList/wishList";
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/wishList/item";
import {WishListItem} from "../../_models/wishList/wishListItem";
import {UserService} from "../../_services/user.service";

@Component({
  selector: 'app-bookeditems',
  templateUrl: './booked-items.component.html',
  styleUrls: ['../wishlist/wishlist.component.css']
})
export class BookedItemsComponent implements OnInit {

  wishList: WishList;
  updatableWishList: WishList;
  hasChanges: boolean = false;
  editableItem: Item;
  copiedItem: Item;
  items: Item[];
  wishListItemView: WishListItem;
  value : number;
  path: string[] = ['item'];
  order: number = 1;
  queryString: string;
  customerLogin: string;
  itemPriorityO: string;

  constructor(private wishListService: WishListService,
              private alertService: AlertService,
              private userService: UserService) {
    this.wishList = new WishList();
    this.updatableWishList = new WishList();
    this.updatableWishList.items = [];
    this.wishListItemView = new WishListItem();
    this.queryString = '';
    this.editableItem = new Item();
    this.copiedItem = new Item();
  }

  ngOnInit() {
    this.getBookedItems();

    this.customerLogin = this.userService.getCurrentLogin();
  }

  getBookedItems(): void {
    this.wishListService.getBookedItems()
      .subscribe((wishList) => {
        this.wishList = wishList;
      }, () => {
      this.alertService.info("Items not found")});
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
    item.priority = +this.itemPriorityO;
    this.updatableWishList.items.push(item);
    this.hasChanges = true;
  }

  sortItems(prop: string) {
    this.path = prop.split('.');
    this.order = this.order * (-1);
    return false;
  }

  editItem(item: Item): void {
      this.editableItem = item;
  }

  isCreator(item: Item): boolean {
    return item.creator_customer_login == this.customerLogin;
  }

  copyItem(item: Item){
      this.copiedItem = item;
  }

  update(): void {
    if (this.updatableWishList.items != null)
    this.wishListService.update(this.updatableWishList).subscribe(() => {
      this.alertService.success('Wish list successfully updated!');
    }, () => {
      this.alertService.error('Something wrong');
    });
    this.hasChanges = false;
  }
}
