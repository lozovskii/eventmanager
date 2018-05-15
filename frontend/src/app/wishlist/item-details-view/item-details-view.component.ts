import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Item} from "../../_models/item";
import {ItemDto} from "../../_models/dto/itemDto";

@Component({
  selector: 'app-item-details-view',
  templateUrl: './item-details-view.component.html'
})
export class ItemDetailsViewComponent implements OnInit, OnChanges {

  @Input() itemView: Item;
  @Input('itemDtoView') itemDtoView: ItemDto;
  item: Item;
  itemDto: ItemDto;

  constructor() {
    this.item = new Item();
    this.itemDto = new ItemDto();
  }

  ngOnInit(): void {}

  ngOnChanges(changes: SimpleChanges) {
    for (let propName in changes) {
      console.log(propName);
      this.itemDto = changes[propName].currentValue;
      this.item = changes[propName].currentValue;
    }
  }

}
