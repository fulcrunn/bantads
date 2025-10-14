import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TelaInicialGerente } from './tela-inicial-gerente';

describe('TelaInicial', () => {
  let component: TelaInicialGerente;
  let fixture: ComponentFixture<TelaInicialGerente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TelaInicialGerente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TelaInicialGerente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
