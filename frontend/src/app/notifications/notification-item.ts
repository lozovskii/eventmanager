import {Type} from '@angular/core';

export class NotificationItem {
  constructor(public component: Type<any>, public data: any) {}
}
