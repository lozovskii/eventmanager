import {Event} from "../event";

export class UpdateEventDTO {
  event: Event;
  newPeople: string[];
  removedPeople: string[];
  frequencyNumber: number;
  frequencyPeriod: string;
  priority: string;
}
