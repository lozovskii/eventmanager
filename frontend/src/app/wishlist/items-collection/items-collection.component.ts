import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/wishList/item";
import {WishList} from "../../_models/wishList/wishList";
import {WishListItem} from "../../_models/wishList/wishListItem";
import {EventService} from "../../_services/event.service";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {Event} from "../../_models/event";

@Component({
  selector: 'app-items-collection',
  templateUrl: './items-collection.component.html',
  styleUrls: ['../wishlist/wishlist.component.css']
})
export class ItemsCollectionComponent implements OnInit {
  @Input('included') isIncluded: boolean;
  @Output('itemView') outItemView = new EventEmitter<Item>();
  @Output('editableItem') outEditableItem = new EventEmitter<Item>();
  @Output('copiedItem') outCopiedItem = new EventEmitter<Item>();
  @Output('eventsDTO') outEventsDTO = new EventEmitter<EventDTOModel[]>();

  hasChanges: boolean = false;
  editableItem: Item;
  copiedItem: Item;
  itemView: Item;
  wishList: WishList;
  trash: Item[];
  items: Item[];
  path: string[] = ['item'];
  order: number = 1;
  queryString: string;
  eventsDTO: EventDTOModel[];

  constructor(private wishListService: WishListService,
              private alertService: AlertService,
              private eventService: EventService) {
    this.items = [];
    this.trash = [];
    this.wishList = new WishList();
    this.editableItem = new Item();
    this.itemView = new Item();
    this.queryString = '';
  }

  ngOnInit() {
    this.wishListService.wishList$.subscribe((wishList) => {
      this.wishList = wishList;
    });
    this.getItemsCollection();
  }

  showMyEvents(item: Item): void {
    this.eventService.getEventsByCustId()
      .subscribe((eventsDTO) => {
        this.isIncluded ?
          this.outEventsDTO.emit(eventsDTO) :
          this.eventsDTO = eventsDTO;
      });
    this.isIncluded ?
      this.outCopiedItem.emit(item) :
      this.copiedItem = item;
  }

  getItemsCollection(): void {
    this.wishListService.getItemsCollection()
      .subscribe((items) => {
        this.items = items;
      }, () => {
        this.alertService.info('Items not found');
      });
  }

  showItemDetails(item: Item): void {
    this.itemView = item;
  }

  addItem(item: Item): void {
    let wishListItem: WishListItem = new WishListItem();
    wishListItem.item = item;
    wishListItem.event_id = this.wishList.id;
    wishListItem.priority = 3;
    this.wishList.items.push(wishListItem);
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

  addCreatedItem(item: Item): void {
    this.items.push(item);
  }

  deleteItem(item: Item): void {
    let index = this.items.indexOf(item);
    this.items.splice(index, 1);
    this.trash.push(item);
    this.hasChanges = true;
  }

  executeDelete(): void {
    if (this.trash.length > 0) {
      this.wishListService.deleteItems(this.trash).subscribe(() =>
          this.alertService.success('Items successfully deleted!'),
        () => this.alertService.error('Something wrong'));
    }
  }

  sortItems(prop: string) {
    this.path = prop.split('.');
    this.order = this.order * (-1);
    return false;
  }
}
