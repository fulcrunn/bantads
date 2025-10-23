import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemocaoGerente } from './remocao-gerente';

describe('RemocaoGerente', () => {
  let component: RemocaoGerente;
  let fixture: ComponentFixture<RemocaoGerente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RemocaoGerente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RemocaoGerente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
