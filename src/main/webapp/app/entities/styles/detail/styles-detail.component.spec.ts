import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StylesDetailComponent } from './styles-detail.component';

describe('Styles Management Detail Component', () => {
  let comp: StylesDetailComponent;
  let fixture: ComponentFixture<StylesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StylesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ styles: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(StylesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StylesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load styles on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.styles).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
