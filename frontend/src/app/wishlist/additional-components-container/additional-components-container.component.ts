import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
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
  @Output('createdItem') createdItem = new EventEmitter<Item>();
  @Output('updatedItem') updatedItem = new EventEmitter<Item>();

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

  addCreatedItem(item: Item) {
    this.createdItem.emit(item);
  }

  updateEditedItem(item: Item) {
    this.updatedItem.emit(item);
  }
}
