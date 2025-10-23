import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlteracaoGerente } from './alteracao-gerente';

describe('AlteracaoGerente', () => {
  let component: AlteracaoGerente;
  let fixture: ComponentFixture<AlteracaoGerente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlteracaoGerente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlteracaoGerente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
