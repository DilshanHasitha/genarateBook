import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BooksDetailComponent } from './books-detail.component';

describe('Books Management Detail Component', () => {
  let comp: BooksDetailComponent;
  let fixture: ComponentFixture<BooksDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BooksDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ books: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BooksDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BooksDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load books on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.books).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
