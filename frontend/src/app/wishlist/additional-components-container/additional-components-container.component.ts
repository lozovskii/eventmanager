import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Item} from "../../_models/wishList/item";
import {WishListItem} from "../../_models/wishList/wishListItem";

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
  item: Item;

  constructor() {
    this.item = new Item();
    this.item.tags = [];
    this.editableItem = new Item();
  }

  ngOnChanges(changes: SimpleChanges) {
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
