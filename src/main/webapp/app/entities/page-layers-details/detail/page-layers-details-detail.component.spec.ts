import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PageLayersDetailsDetailComponent } from './page-layers-details-detail.component';

describe('PageLayersDetails Management Detail Component', () => {
  let comp: PageLayersDetailsDetailComponent;
  let fixture: ComponentFixture<PageLayersDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PageLayersDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pageLayersDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PageLayersDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PageLayersDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pageLayersDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pageLayersDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
