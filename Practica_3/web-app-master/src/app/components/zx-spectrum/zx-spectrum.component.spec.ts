import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ZxSpectrumComponent } from './zx-spectrum.component';

describe('ZxSpectrumComponent', () => {
  let component: ZxSpectrumComponent;
  let fixture: ComponentFixture<ZxSpectrumComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ZxSpectrumComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ZxSpectrumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
