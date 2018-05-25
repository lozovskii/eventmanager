import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/wishList/item";
import {UserService} from "../../_services/user.service";
import {WishList} from "../../_models/wishList/wishList";
import {WishListItem} from "../../_models/wishList/wishListItem";
import {EventService} from "../../_services/event.service";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {Event} from "../../_models/event";
import {Subscription} from "rxjs/Rx";

@Component({
  selector: 'additional-components-container',
  templateUrl: './additional-components-container.component.html',
  styleUrls: ['../wishlist/wishlist.component.css']
})
export class AdditionalComponentsContainerComponent implements OnChanges {
  @Input('editableItem') inEditableItem: Item;
  @Input('eventsDTO') inEventsDto: EventDTOModel;
  @Input('copiedItem') inCopiedItem: Item;
  @Input('movableItem') inMovableItem: WishListItem;

  editableItem: Item;
  copiedItem: Item;
  movableItem: WishListItem;
  wishList: WishList;
  item: Item;
  items: Item[];
  eventsDTO: EventDTOModel[];

  constructor(private wishListService: WishListService,
              private alertService: AlertService) {
    this.items = [];
    this.item = new Item();
    this.item.tags = [];
    this.editableItem = new Item();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.editableItem = new Item();

    for (let prop in changes) {
      switch (prop) {
        case 'inEditableItem' : {
          this.editableItem = changes[prop].currentValue;
          break;
        }
        case 'inEventsDto' : {
          this.eventsDTO = changes[prop].currentValue;
          break;
        }
        case 'inCopiedItem' : {
          this.copiedItem = changes[prop].currentValue;
          break;
        }
        case 'inMovableItem' : {
          this.movableItem = changes[prop].currentValue;
        }
      }
    }
  }

  copyToEventWishList(event: Event): void {
    let wishList = new WishList();
    let wishListItem: WishListItem = new WishListItem();
    wishListItem.item = new Item();
    wishListItem.item = this.movableItem ? this.movableItem.item : this.copiedItem;
    wishListItem.event_id = event.id;
    wishListItem.priority = 3;
    wishList.id = event.id;
    wishList.items = [];
    wishList.items.push(wishListItem);

    this.wishListService.addItems(wishList)
      .subscribe(() => {
        if (this.copiedItem)
          this.alertService.success('Item successfully copied to wishlist!');
        else if (this.movableItem) {
          this.wishListService.removeItems([this.movableItem])
            .subscribe(() => {
              let s: Subscription = this.wishListService.wishList$.subscribe((wishList) => {
                this.wishList = wishList;
                let index = this.wishList.items.indexOf(this.movableItem);
                this.wishList.items.splice(index, 1);
              });
              s.unsubscribe();
              this.alertService.success('Item successfully moved to wishlist!')
            });
        }
      }, () => {
        this.alertService.error('Something wrong')
      });
  }

  // TODO implement it
  addCreatedItem(item: Item) {

  }

  updateEditedItem(item: Item) {

  }
}
