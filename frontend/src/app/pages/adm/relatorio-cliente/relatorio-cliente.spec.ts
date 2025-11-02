import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RelatorioCliente } from './relatorio-cliente';

describe('RelatorioCliente', () => {
  let component: RelatorioCliente;
  let fixture: ComponentFixture<RelatorioCliente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RelatorioCliente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RelatorioCliente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
