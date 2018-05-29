import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {WishList} from "../../_models/wishList/wishList";
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {UserService} from "../../_services/user.service";
import {WishListItem} from "../../_models/wishList/wishListItem";
import {Item} from "../../_models/wishList/item";

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishListComponent implements OnInit {
  @Input('eventId') eventId: string;
  @Input('isCreator') isEventCreator: boolean = false;
  @Input('editMode') editMode: boolean = false;
  @Output('wishListItemView') outWishListItemView = new EventEmitter<WishListItem>();
  @Output('editableItem') outEditableItem = new EventEmitter<Item>();
  @Output('copiedItem') outCopiedItem = new EventEmitter<Item>();
  @Output('movableItem') outMovableItem = new EventEmitter<WishListItem>();

  trash: WishListItem[];
  wishList: WishList;
  wishListItemView: WishListItem;
  editableItem: Item;
  copiedItem: Item;
  movableItem: WishListItem;
  currentLogin: string;
  hasChanges: boolean = false;
  path: string[] = [''];
  order: number = 1;
  queryString: string;

  constructor(private wishListService: WishListService,
              private userService: UserService,
              private alertService: AlertService) {
    this.queryString = '';
    this.editableItem = new Item();
  }

  ngOnInit() {
    if (this.editMode) {
      this.wishListService.wishList$.subscribe(wishList => this.wishList = wishList);
    } else {
      this.getWishListByEventId(this.eventId);
    }
    this.currentLogin = this.userService.getCurrentLogin();
    this.trash = [];
    this.wishListItemView = new WishListItem();
  }

  copyItem(item: Item){
    this.editMode ?
      this.outCopiedItem.emit(item) :
      this.copiedItem = item;
  }

  moveItem(wishListItem: WishListItem){
    this.editMode ?
      this.outMovableItem.emit(wishListItem) :
      this.movableItem = wishListItem;
  }

  // editItem(item: Item): void {
  //   this.outEditableItem.emit(item);
  // }

  showItemDetails(item: WishListItem): void {
      this.wishListItemView = item;
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
      this.wishListService.removeItems(this.trash).subscribe(() => {
          this.alertService.success('Wish list successfully updated!')
        },
        () => {
          this.alertService.error('Something wrong')
        });
    }
    if (this.wishList.items != null)
      this.wishListService.addItems(this.wishList).subscribe(() => {
        this.alertService.success('Wish list successfully updated!');
      }, () => {
        this.alertService.error('Something wrong');
      });

    this.hasChanges = false;
  }

  removeItem(wishListItem: WishListItem): void {
    let index = this.wishList.items.indexOf(wishListItem);
    this.wishList.items.splice(index, 1);
    this.trash.push(wishListItem);
    this.hasChanges = true;
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

  updatePriority(wishListItem: WishListItem): void {
    this.hasChanges = true;
  }

  addCreatedItem(item: Item): void {
    let wishListItem: WishListItem = new WishListItem();
    wishListItem.item = item;
    wishListItem.event_id = this.wishList.id;
    wishListItem.priority = 3;
    this.wishList.items.push(wishListItem);
  }

  isCreator(item: Item): boolean {
    return item.creator_customer_login == this.currentLogin;
  }

  sortItems(prop: string) {
    this.path = prop.split('.');
    this.order = this.order * (-1);
    return false;
  }

  update(): void {
    this.wishListService.update(this.wishList).subscribe(() => {
      this.alertService.success('Wish list successfully updated!');
    }, () => {
      this.alertService.error('Something wrong');
    });
    this.hasChanges = false;
  }

  isBooker(bookerLogin: string): boolean {
    return this.wishListService.isBooker(bookerLogin);
  }
}
