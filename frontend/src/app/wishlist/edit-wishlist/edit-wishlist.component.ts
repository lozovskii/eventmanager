import {Component} from '@angular/core';
import {Item} from "../../_models/wishList/item";
import {EventDTOModel} from "../../_models/dto/eventDTOModel";

@Component({
  selector: 'app-edit-wishList',
  templateUrl: './edit-wishlist.component.html'
})
export class EditWishListComponent {

  editableItem: Item;
  copiedItem: Item;
  eventsDTO: EventDTOModel[];

  constructor() {}

  setEditableItem(editableItem: Item){
    this.editableItem = editableItem;
  }

  setCopiedItem(copiedItem: Item){
    this.copiedItem = copiedItem;
  }

  setEventsDTO(eventsDTO: EventDTOModel[]){
    this.eventsDTO = eventsDTO;
  }
}
