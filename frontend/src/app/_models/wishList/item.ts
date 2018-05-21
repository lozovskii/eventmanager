import {ExtendedTag} from "./extendedTag";
import {ItemRater} from "./itemRater";

export class Item{
  id: string;
  creator_customer_login : string;
  name: string;
  description : string;
  image : string;
  link : string;
  dueDate : Date;
  tags: ExtendedTag[];
  raters: ItemRater[];
}
