import {Event} from "../event";
import {Location} from "../../_models/location";


export class UpdateEventDTO {
  event: Event;
  newPeople: string[];
  removedPeople: string[];
  frequencyNumber: number;
  frequencyPeriod: string;
  priority: string;
  startTimeNotification: string;
  location: Location;
}
