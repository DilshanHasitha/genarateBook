import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SelectedOptionDetailsDetailComponent } from './selected-option-details-detail.component';

describe('SelectedOptionDetails Management Detail Component', () => {
  let comp: SelectedOptionDetailsDetailComponent;
  let fixture: ComponentFixture<SelectedOptionDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SelectedOptionDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ selectedOptionDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SelectedOptionDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SelectedOptionDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load selectedOptionDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.selectedOptionDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
