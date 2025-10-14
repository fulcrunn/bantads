import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AprovarCliente } from './aprovar-cliente';

describe('AprovarCliente', () => {
  let component: AprovarCliente;
  let fixture: ComponentFixture<AprovarCliente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AprovarCliente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AprovarCliente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
