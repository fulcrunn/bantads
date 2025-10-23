import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TelaInicialAdm } from './tela-inicial-adm';

describe('TelaInicialAdm', () => {
  let component: TelaInicialAdm;
  let fixture: ComponentFixture<TelaInicialAdm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TelaInicialAdm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TelaInicialAdm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
