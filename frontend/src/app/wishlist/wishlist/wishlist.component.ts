import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {WishList} from "../../_models/wishList/wishList";
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {UserService} from "../../_services/user.service";
import {WishListItem} from "../../_models/wishList/wishListItem";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishListComponent implements OnInit {
  @Input('eventId') eventId: string;
  @Input('editMode') editMode: boolean = false;

  filterInput = new FormControl();
  filterText: string;
  filterPlaceholder: string;
  trash: WishListItem[];
  wishList: WishList;
  wishListItemView: WishListItem;
  currentLogin: string;
  hasChanges: boolean = false;
  path: string[] = ['name'];
  order: number = 1;

  constructor(private wishListService: WishListService,
              private userService: UserService,
              private alertService: AlertService) {}

  ngOnInit() {
    if (this.editMode) {
      this.wishListService.wishList$.subscribe(wishList => this.wishList = wishList);
    } else {
      this.getWishListByEventId(this.eventId);
    }
    this.currentLogin = this.userService.getCurrentLogin();
    this.trash = [];
    this.wishListItemView = new WishListItem();

    this.filterText = '';
    this.filterPlaceholder = 'You can filter values by name, description, link and creator login';
    this.filterInput
      .valueChanges
      .debounceTime(200)
      .subscribe(term => {
        this.filterText = term;
      });
  }

  showItemDetails(wishListItem: WishListItem): void {
    this.wishListItemView = wishListItem;
  }

  getWishListByEventId(eventId: string): void {
    this.wishListService.getByEventId(eventId)
      .subscribe((wishList) => {
          this.wishList = wishList;
          this.wishListService.setCurrentWishList(this.wishList);
        }, () => {
          this.wishList = new WishList();
          this.wishList.id = this.eventId;
          this.wishList.items = [];
          this.wishListService.setCurrentWishList(this.wishList);
        }
      );
  }

  updateWishList(): void {
    if (this.trash.length > 0) {
      this.wishListService.removeItems(this.trash).subscribe(() =>
          this.alertService.success('Wish list successfully updated!'),
        () => this.alertService.error('Something wrong'));
    }
    if (this.wishList.items != null)
      this.wishListService.addItems(this.wishList).subscribe(() => {
        this.alertService.success('Wish list successfully updated!');
      }, () => {
        this.alertService.error('Something wrong');
      }, () => {
        this.alertService.success('Wish list successfully updated!');
      });
  }

  removeItem(wishListItem: WishListItem): void {
    this.hasChanges = true;
    let index = this.wishList.items.indexOf(wishListItem);
    this.wishList.items.splice(index, 1);
    this.trash.push(wishListItem);
  }

  bookItem(wishListItem: WishListItem): void {
    wishListItem.booker_customer_login = this.currentLogin;
    this.hasChanges = true;
  }

  cancelBooking(wishListItem: WishListItem): void {
    wishListItem.booker_customer_login = null;
    wishListItem.priority = 3;
    this.hasChanges = true;
  }

  updatePriority(wishListItem: WishListItem): void{
    this.hasChanges = true;
  }

  sortItems(prop: string) {
    this.path = prop.split('.');
    this.order = this.order * (-1); // change order
    return false; // do not reload
  }

  update(): void {
    this.wishListService.update(this.wishList).subscribe(() => {
      this.alertService.success('Wish list successfully updated!');
    }, () => {
      this.alertService.error('Something wrong');
    });
  }

  isBooker(bookerLogin: string): boolean {
    return this.wishListService.isBooker(bookerLogin);
  }
}
