import {Directive, ViewContainerRef} from '@angular/core';

@Directive({
  selector: '[appNotificationsHost]'
})
export class NotificationsHostDirective {

  constructor(public viewContainerRef: ViewContainerRef) { }

}
