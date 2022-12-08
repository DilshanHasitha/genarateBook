import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BooksRelatedOptionDetailComponent } from './books-related-option-detail.component';

describe('BooksRelatedOption Management Detail Component', () => {
  let comp: BooksRelatedOptionDetailComponent;
  let fixture: ComponentFixture<BooksRelatedOptionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BooksRelatedOptionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ booksRelatedOption: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BooksRelatedOptionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BooksRelatedOptionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load booksRelatedOption on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.booksRelatedOption).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
