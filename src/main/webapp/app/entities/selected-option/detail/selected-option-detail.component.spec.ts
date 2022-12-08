import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SelectedOptionDetailComponent } from './selected-option-detail.component';

describe('SelectedOption Management Detail Component', () => {
  let comp: SelectedOptionDetailComponent;
  let fixture: ComponentFixture<SelectedOptionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SelectedOptionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ selectedOption: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SelectedOptionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SelectedOptionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load selectedOption on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.selectedOption).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
