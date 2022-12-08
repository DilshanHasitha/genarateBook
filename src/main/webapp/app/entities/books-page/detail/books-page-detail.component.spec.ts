import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BooksPageDetailComponent } from './books-page-detail.component';

describe('BooksPage Management Detail Component', () => {
  let comp: BooksPageDetailComponent;
  let fixture: ComponentFixture<BooksPageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BooksPageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ booksPage: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BooksPageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BooksPageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load booksPage on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.booksPage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
