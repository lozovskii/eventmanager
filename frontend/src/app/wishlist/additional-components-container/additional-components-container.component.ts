import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {AlertService} from "../../_services/alert.service";
import {WishListService} from "../../_services/wishlist.service";
import {Item} from "../../_models/wishList/item";
import {WishList} from "../../_models/wishList/wishList";
import {WishListItem} from "../../_models/wishList/wishListItem";
import {Event} from "../../_models/event";
import {Subscription} from "rxjs/Rx";

@Component({
  selector: 'additional-components-container',
  templateUrl: './additional-components-container.component.html',
  styleUrls: ['../wishlist/wishlist.component.css']
})
export class AdditionalComponentsContainerComponent implements OnChanges {
  @Input('editableItem') inEditableItem: Item;
  @Input('copiedItem') inCopiedItem: Item;
  @Input('movableItem') inMovableItem: WishListItem;

  editableItem: Item;
  copiedItem: Item;
  movableItem: WishListItem;
  wishList: WishList;
  item: Item;
  items: Item[];

  constructor() {
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

  // TODO implement it
  addCreatedItem(item: Item) {

  }

  updateEditedItem(item: Item) {

  }
}
