import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BooksVariablesDetailComponent } from './books-variables-detail.component';

describe('BooksVariables Management Detail Component', () => {
  let comp: BooksVariablesDetailComponent;
  let fixture: ComponentFixture<BooksVariablesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BooksVariablesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ booksVariables: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BooksVariablesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BooksVariablesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load booksVariables on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.booksVariables).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
