import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WishListComponent } from './wishlist.component';

describe('WishlistComponent', () => {
  let component: WishListComponent;
  let fixture: ComponentFixture<WishListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WishListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WishListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
