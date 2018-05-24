import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {WishList} from "../../_models/wishList/wishList";
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {UserService} from "../../_services/user.service";
import {WishListItem} from "../../_models/wishList/wishListItem";
import {Item} from "../../_models/wishList/item";
import {EventService} from "../../_services/event.service";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {Event} from "../../_models/event";

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishListComponent implements OnInit {
  @Input('eventId') eventId: string;
  @Input('editMode') editMode: boolean = false;
  @Output('wishListItemView') outWishListItemView = new EventEmitter<WishListItem>();
  @Output('editableItem') outEditableItem = new EventEmitter<Item>();
  @Output('copiedItem') outCopiedItem = new EventEmitter<Item>();
  @Output('eventsDTO') outEventsDTO = new EventEmitter<EventDTOModel[]>();

  trash: WishListItem[];
  wishList: WishList;
  wishListItemView: WishListItem;
  editableItem: Item;
  copiedItem: Item;
  currentLogin: string;
  hasChanges: boolean = false;
  path: string[] = ['name'];
  order: number = 1;
  queryString: string;
  eventsDTO: EventDTOModel[];

  constructor(private wishListService: WishListService,
              private userService: UserService,
              private alertService: AlertService,
              private eventService: EventService) {
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

  getMyEvents(item: Item): void {
    this.eventService.getEventsByCustId()
      .subscribe((eventsDTO) => {
        this.editMode ?
          this.outEventsDTO.emit(eventsDTO) :
          this.eventsDTO = eventsDTO;
      });
    this.editMode ?
      this.outCopiedItem.emit(item) :
      this.copiedItem = item;
  }

  editItem(item: Item): void {
    this.outEditableItem.emit(item);
  }

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
      }, () => {
        this.alertService.success('Wish list successfully updated!');
      });

    this.hasChanges = false;
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
