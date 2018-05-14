import {Component, Input, OnInit} from '@angular/core';
import {WishList} from "../../_models/wishlist";
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {UserService} from "../../_services/user.service";
import {ItemDto} from "../../_models/dto/itemDto";

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishListComponent implements OnInit {
  @Input('eventId') eventId: string;
  @Input('included') isIncluded: boolean;

  trash: ItemDto[];
  wishList: WishList;
  hideWishListTab: boolean = false;
  currentLogin: string;
  hasChanges: boolean = false;
  path: string[] = ['name'];
  order: number = 1; // 1 asc, -1 desc;

  constructor(private wishListService: WishListService,
              private userService: UserService,
              private alertService: AlertService) {
    this.wishList = new WishList();
  }

  ngOnInit() {
    if (this.isIncluded || this.wishList != null)
      this.wishListService.currentWishList.subscribe(wishList => this.wishList = wishList);
    else {
      this.getWishListByEventId(this.eventId);
    }
    this.trash = [];
  }

  getWishListByEventId(eventId: string): void {
    this.wishListService.getByEventId(eventId)
      .subscribe((wishList) => {
          this.wishList = wishList;
          this.wishListService.setCurrentWishList(wishList);
        }, () => {
          this.hideWishListTab = true;
        }
      );
  }

  updateWishList(): void {
    if (this.trash.length > 0) {
      this.wishListService.deleteItems(this.trash).subscribe(() =>
          this.alertService.success('Wish list successfully updated!'),
        () => this.alertService.error('Something wrong'));
    }

    this.wishListService.addItems(this.wishList).subscribe(() => {
      this.alertService.success('Wish list successfully updated!');
    }, () => {
      this.alertService.error('Something wrong');
    });
  }

  removeItem(itemDto: ItemDto): void {
    this.hasChanges = true;
    let index = this.wishList.items.indexOf(itemDto);
    this.wishList.items.splice(index, 1);
    this.trash.push(itemDto);
  }

  checkBooker(bookerLogin: string): boolean {
    return bookerLogin == this.currentLogin;
  }

  bookItem(item: ItemDto): void {
    item.booker_customer_login = this.currentLogin;
    this.hasChanges = true;
  }

  cancelBooking(item: ItemDto): void {
    item.booker_customer_login = null;
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
