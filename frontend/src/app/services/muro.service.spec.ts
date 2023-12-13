import { TestBed } from '@angular/core/testing';

import { MuroService } from './muro.service';

describe('MuroService', () => {
  let service: MuroService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MuroService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
