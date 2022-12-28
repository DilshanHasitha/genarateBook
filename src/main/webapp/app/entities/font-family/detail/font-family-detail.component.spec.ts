import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FontFamilyDetailComponent } from './font-family-detail.component';

describe('FontFamily Management Detail Component', () => {
  let comp: FontFamilyDetailComponent;
  let fixture: ComponentFixture<FontFamilyDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FontFamilyDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fontFamily: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FontFamilyDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FontFamilyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fontFamily on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fontFamily).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
