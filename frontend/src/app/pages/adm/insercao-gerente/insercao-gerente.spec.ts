import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InsercaoGerente } from './insercao-gerente';

describe('InsercaoGerente', () => {
  let component: InsercaoGerente;
  let fixture: ComponentFixture<InsercaoGerente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InsercaoGerente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InsercaoGerente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
