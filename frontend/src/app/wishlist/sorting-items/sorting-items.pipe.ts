import { Pipe, PipeTransform } from '@angular/core';
import {Item} from "../../_models/item";

@Pipe({
  name: 'sortingItems'
})
export class SortingItemsPipe implements PipeTransform {

  transform(items: Item[], path: string[], order: number): Item[] {

    if (!items || !path || !order) return items;

    return items.sort((a, b) => {

      path.forEach(property => {
        a = a[property];
        b = b[property];
      });

      return a > b ? order : order * -1;
    });

  }

}
