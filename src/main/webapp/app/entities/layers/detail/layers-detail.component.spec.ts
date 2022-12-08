import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LayersDetailComponent } from './layers-detail.component';

describe('Layers Management Detail Component', () => {
  let comp: LayersDetailComponent;
  let fixture: ComponentFixture<LayersDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LayersDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ layers: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LayersDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LayersDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load layers on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.layers).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
