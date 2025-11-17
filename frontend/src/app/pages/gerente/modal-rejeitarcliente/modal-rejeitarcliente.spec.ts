import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalRejeitarcliente } from './modal-rejeitarcliente';

describe('ModalRejeitarcliente', () => {
  let component: ModalRejeitarcliente;
  let fixture: ComponentFixture<ModalRejeitarcliente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalRejeitarcliente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalRejeitarcliente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
