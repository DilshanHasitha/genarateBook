import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StylesDetailsDetailComponent } from './styles-details-detail.component';

describe('StylesDetails Management Detail Component', () => {
  let comp: StylesDetailsDetailComponent;
  let fixture: ComponentFixture<StylesDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StylesDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ stylesDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(StylesDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StylesDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load stylesDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.stylesDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
