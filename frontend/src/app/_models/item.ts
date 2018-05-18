import {ItemTagDto} from "./dto/itemTagDto";

export class Item{
  id: string;
  creator_customer_login : string;
  name: string;
  description : string;
  image : string;
  link : string;
  dueDate : Date;
  tags: ItemTagDto[];
}
