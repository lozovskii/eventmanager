import {Component} from '@angular/core';
import {Item} from "../../_models/wishList/item";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";
import {WishListItem} from "../../_models/wishList/wishListItem";

@Component({
  selector: 'app-edit-wishList',
  templateUrl: './edit-wishlist.component.html'
})
export class EditWishListComponent {

  editableItem: Item;
  copiedItem: Item;
  movableItem : WishListItem;
  eventsDTO: EventDTOModel[];

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

  setEventsDTO(eventsDTO: EventDTOModel[]){
    this.eventsDTO = eventsDTO;
  }
}
