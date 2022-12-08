import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OptionTypeDetailComponent } from './option-type-detail.component';

describe('OptionType Management Detail Component', () => {
  let comp: OptionTypeDetailComponent;
  let fixture: ComponentFixture<OptionTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OptionTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ optionType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OptionTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OptionTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load optionType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.optionType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
