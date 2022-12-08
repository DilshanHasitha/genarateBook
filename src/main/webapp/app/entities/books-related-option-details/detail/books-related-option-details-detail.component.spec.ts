import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BooksRelatedOptionDetailsDetailComponent } from './books-related-option-details-detail.component';

describe('BooksRelatedOptionDetails Management Detail Component', () => {
  let comp: BooksRelatedOptionDetailsDetailComponent;
  let fixture: ComponentFixture<BooksRelatedOptionDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BooksRelatedOptionDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ booksRelatedOptionDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BooksRelatedOptionDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BooksRelatedOptionDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load booksRelatedOptionDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.booksRelatedOptionDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
