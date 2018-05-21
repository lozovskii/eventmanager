import {Location} from "../_models/location";


export class AdditionEventModel {
  people: string[];
  frequencyNumber: number;
  frequencyPeriod: string;
  priority: string;
  startTimeNotification: string;
  location: Location;
}
