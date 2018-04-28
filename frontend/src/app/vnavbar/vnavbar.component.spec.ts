import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VnavbarComponent } from './vnavbar.component';

describe('VnavbarComponent', () => {
  let component: VnavbarComponent;
  let fixture: ComponentFixture<VnavbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VnavbarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VnavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
