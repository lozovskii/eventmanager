import {AdditionEventModel} from "../additionEvent.model";
import {Event} from "../event";
import {UpdateEventDTO} from "./UpdateEventDTO";

export class EventDTOModel {
  event: Event;
  additionEvent: AdditionEventModel;
}
