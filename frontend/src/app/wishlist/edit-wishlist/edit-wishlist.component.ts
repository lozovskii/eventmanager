import {Component} from '@angular/core';
import {Item} from "../../_models/wishList/item";
import {WishListItem} from "../../_models/wishList/wishListItem";

@Component({
  selector: 'app-edit-wishList',
  templateUrl: './edit-wishlist.component.html'
})
export class EditWishListComponent {

  editableItem: Item;
  copiedItem: Item;
  movableItem : WishListItem;

  constructor() {}

  setEditableItem(editableItem: Item){
    this.editableItem = editableItem;
  }

  setCopiedItem(copiedItem: Item){
    this.copiedItem = copiedItem;
  }

  setMovableItem(movableItem: WishListItem){
    this.movableItem = movableItem;
  }
}
