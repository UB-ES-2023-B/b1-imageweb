import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlbumViewEditModeComponent } from './album-view-edit-mode.component';

describe('AlbumViewEditModeComponent', () => {
  let component: AlbumViewEditModeComponent;
  let fixture: ComponentFixture<AlbumViewEditModeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AlbumViewEditModeComponent]
    });
    fixture = TestBed.createComponent(AlbumViewEditModeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
