import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BooksOptionDetailsDetailComponent } from './books-option-details-detail.component';

describe('BooksOptionDetails Management Detail Component', () => {
  let comp: BooksOptionDetailsDetailComponent;
  let fixture: ComponentFixture<BooksOptionDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BooksOptionDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ booksOptionDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BooksOptionDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BooksOptionDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load booksOptionDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.booksOptionDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
