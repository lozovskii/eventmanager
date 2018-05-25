import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/wishList/item";
import {UserService} from "../../_services/user.service";
import {WishList} from "../../_models/wishList/wishList";
import {WishListItem} from "../../_models/wishList/wishListItem";
import {EventService} from "../../_services/event.service";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {Event} from "../../_models/event";

@Component({
  selector: 'app-all-items',
  templateUrl: './all-items.component.html',
  styleUrls: ['../wishlist/wishlist.component.css']
})
export class AllItemsComponent implements OnInit {
  @Input('included') isIncluded: boolean = false;
  @Output('itemView') outItemView = new EventEmitter<Item>();
  @Output('editableItem') outEditableItem = new EventEmitter<Item>();
  @Output('copiedItem') outCopiedItem = new EventEmitter<Item>();
  @Output('eventsDTO') outEventsDTO = new EventEmitter<EventDTOModel[]>();

  itemView: Item;
  editableItem: Item;
  copiedItem: Item;
  wishList: WishList;
  item: Item;
  items: Item[];
  path: string[] = ['name'];
  order: number = 1;
  customerLogin: string;
  queryString: string;
  eventsDTO: EventDTOModel[];

  constructor(private wishListService: WishListService,
              private userService: UserService,
              private alertService: AlertService,
              private eventService: EventService) {
    this.items = [];
    this.item = new Item();
    this.item.tags = [];
    this.itemView = new Item();
    this.editableItem = new Item();
    this.queryString = '';
  }

  ngOnInit() {
    if (this.isIncluded)
      this.wishListService.wishList$.subscribe((wishList) => {
        this.wishList = wishList;
      });

    this.customerLogin = JSON.parse(sessionStorage.getItem('currentUser')).login;

    this.getAllItems();
  }

  getMyEvents(): void {
    this.eventService.getEventsByCustId()
      .subscribe((eventsDTO) => {
        this.isIncluded ?
          this.outEventsDTO.emit(eventsDTO) :
          this.eventsDTO = eventsDTO;
      });
  }

  copyItem(item: Item){
    this.getMyEvents();
    this.isIncluded ?
      this.outCopiedItem.emit(item) :
      this.copiedItem = item;
  }

  isCreator(item: Item): boolean {
    return item.creator_customer_login == this.customerLogin;
  }

  editItem(item: Item): void {
    this.isIncluded ?
      this.outEditableItem.emit(item) :
      this.editableItem = item;
  }

  updateEditedItem(item: Item): void {
    let index = this.items.indexOf(this.editableItem);
    this.items.fill(item, index, index + 1);
  }


  showItemDetails(item: Item): void {
    this.itemView = item;
  }

  addCreatedItem(item: Item): void {
    this.items.push(item);
  }

  addItem(item: Item): void {
    let wishListItem: WishListItem = new WishListItem();
    wishListItem.item = item;
    wishListItem.event_id = this.wishList.id;
    wishListItem.priority = 3;
    this.wishList.items.push(wishListItem);
  }

  deleteItem(item: Item): void {
    let index = this.items.indexOf(item);
    this.items.splice(index, 1);
    let trash: Item[] = [];
    trash.push(item);
    this.wishListService.deleteItems(trash).subscribe(() =>
        this.alertService.success('Item successfully deleted!'),
      () => this.alertService.error('Something wrong'));
  }

  addToCollection(item: Item): void {
    delete item.id;
    item.creator_customer_login = this.userService.getCurrentLogin();
    this.wishListService.createItem(item)
      .subscribe(() => {
          this.alertService.success('Item added to you Collection.');
        },
        () => {
          this.alertService.error("Something wrong");
        });
  }

  getAllItems(): void {
    this.wishListService.getAllItems()
      .subscribe((items) => {
        this.items = items;
      }, () => {
        this.alertService.info('Items not found');
      });
  }

  sortItems(prop: string) {
    this.path = prop.split('.');
    this.order = this.order * (-1); // change order
    return false; // do not reload
  }
}
