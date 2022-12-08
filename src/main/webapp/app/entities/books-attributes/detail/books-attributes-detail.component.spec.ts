import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BooksAttributesDetailComponent } from './books-attributes-detail.component';

describe('BooksAttributes Management Detail Component', () => {
  let comp: BooksAttributesDetailComponent;
  let fixture: ComponentFixture<BooksAttributesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BooksAttributesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ booksAttributes: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BooksAttributesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BooksAttributesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load booksAttributes on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.booksAttributes).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
