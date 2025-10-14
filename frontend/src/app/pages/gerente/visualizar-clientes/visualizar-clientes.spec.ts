import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualizarClientes } from './visualizar-clientes';

describe('VisualizarClientes', () => {
  let component: VisualizarClientes;
  let fixture: ComponentFixture<VisualizarClientes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VisualizarClientes]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VisualizarClientes);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
