import { TestBed } from '@angular/core/testing';

import { Locationservice } from './locationservice';

describe('Locationservice', () => {
  let service: Locationservice;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Locationservice);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
