import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LayerGroupDetailComponent } from './layer-group-detail.component';

describe('LayerGroup Management Detail Component', () => {
  let comp: LayerGroupDetailComponent;
  let fixture: ComponentFixture<LayerGroupDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LayerGroupDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ layerGroup: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LayerGroupDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LayerGroupDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load layerGroup on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.layerGroup).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
