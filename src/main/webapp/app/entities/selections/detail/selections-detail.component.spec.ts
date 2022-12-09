import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SelectionsDetailComponent } from './selections-detail.component';

describe('Selections Management Detail Component', () => {
  let comp: SelectionsDetailComponent;
  let fixture: ComponentFixture<SelectionsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SelectionsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ selections: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SelectionsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SelectionsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load selections on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.selections).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
