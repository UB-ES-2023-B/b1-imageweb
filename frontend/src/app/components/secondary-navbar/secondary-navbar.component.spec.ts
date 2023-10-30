import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SecondaryNavbarComponent } from './secondary-navbar.component';

describe('SecondaryNavbarComponent', () => {
  let component: SecondaryNavbarComponent;
  let fixture: ComponentFixture<SecondaryNavbarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SecondaryNavbarComponent]
    });
    fixture = TestBed.createComponent(SecondaryNavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
