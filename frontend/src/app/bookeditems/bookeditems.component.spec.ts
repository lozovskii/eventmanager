import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BookedItemsComponent } from './bookeditems.component';

describe('BookedItemsComponent', () => {
  let component: BookedItemsComponent;
  let fixture: ComponentFixture<BookedItemsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BookedItemsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookedItemsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
