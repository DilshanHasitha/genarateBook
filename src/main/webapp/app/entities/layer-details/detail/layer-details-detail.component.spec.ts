import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LayerDetailsDetailComponent } from './layer-details-detail.component';

describe('LayerDetails Management Detail Component', () => {
  let comp: LayerDetailsDetailComponent;
  let fixture: ComponentFixture<LayerDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LayerDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ layerDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LayerDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LayerDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load layerDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.layerDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
