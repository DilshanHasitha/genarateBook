import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PageLayersDetailComponent } from './page-layers-detail.component';

describe('PageLayers Management Detail Component', () => {
  let comp: PageLayersDetailComponent;
  let fixture: ComponentFixture<PageLayersDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PageLayersDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pageLayers: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PageLayersDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PageLayersDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pageLayers on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pageLayers).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
