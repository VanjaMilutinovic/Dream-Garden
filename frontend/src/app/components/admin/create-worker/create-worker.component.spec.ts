import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateWorkerComponent } from './create-worker.component';

describe('CreateWorkerComponent', () => {
  let component: CreateWorkerComponent;
  let fixture: ComponentFixture<CreateWorkerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateWorkerComponent]
    });
    fixture = TestBed.createComponent(CreateWorkerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
