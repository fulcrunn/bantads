import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListagemGerente } from './listagem-gerente';

describe('ListagemGerente', () => {
  let component: ListagemGerente;
  let fixture: ComponentFixture<ListagemGerente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListagemGerente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListagemGerente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
