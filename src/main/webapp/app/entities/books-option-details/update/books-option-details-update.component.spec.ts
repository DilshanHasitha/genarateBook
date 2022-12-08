import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BooksOptionDetailsFormService } from './books-option-details-form.service';
import { BooksOptionDetailsService } from '../service/books-option-details.service';
import { IBooksOptionDetails } from '../books-option-details.model';
import { IBooks } from 'app/entities/books/books.model';
import { BooksService } from 'app/entities/books/service/books.service';

import { BooksOptionDetailsUpdateComponent } from './books-option-details-update.component';

describe('BooksOptionDetails Management Update Component', () => {
  let comp: BooksOptionDetailsUpdateComponent;
  let fixture: ComponentFixture<BooksOptionDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let booksOptionDetailsFormService: BooksOptionDetailsFormService;
  let booksOptionDetailsService: BooksOptionDetailsService;
  let booksService: BooksService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BooksOptionDetailsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BooksOptionDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BooksOptionDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    booksOptionDetailsFormService = TestBed.inject(BooksOptionDetailsFormService);
    booksOptionDetailsService = TestBed.inject(BooksOptionDetailsService);
    booksService = TestBed.inject(BooksService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Books query and add missing value', () => {
      const booksOptionDetails: IBooksOptionDetails = { id: 456 };
      const books: IBooks = { id: 9032 };
      booksOptionDetails.books = books;

      const booksCollection: IBooks[] = [{ id: 45893 }];
      jest.spyOn(booksService, 'query').mockReturnValue(of(new HttpResponse({ body: booksCollection })));
      const additionalBooks = [books];
      const expectedCollection: IBooks[] = [...additionalBooks, ...booksCollection];
      jest.spyOn(booksService, 'addBooksToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ booksOptionDetails });
      comp.ngOnInit();

      expect(booksService.query).toHaveBeenCalled();
      expect(booksService.addBooksToCollectionIfMissing).toHaveBeenCalledWith(
        booksCollection,
        ...additionalBooks.map(expect.objectContaining)
      );
      expect(comp.booksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const booksOptionDetails: IBooksOptionDetails = { id: 456 };
      const books: IBooks = { id: 61491 };
      booksOptionDetails.books = books;

      activatedRoute.data = of({ booksOptionDetails });
      comp.ngOnInit();

      expect(comp.booksSharedCollection).toContain(books);
      expect(comp.booksOptionDetails).toEqual(booksOptionDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksOptionDetails>>();
      const booksOptionDetails = { id: 123 };
      jest.spyOn(booksOptionDetailsFormService, 'getBooksOptionDetails').mockReturnValue(booksOptionDetails);
      jest.spyOn(booksOptionDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksOptionDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booksOptionDetails }));
      saveSubject.complete();

      // THEN
      expect(booksOptionDetailsFormService.getBooksOptionDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(booksOptionDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(booksOptionDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksOptionDetails>>();
      const booksOptionDetails = { id: 123 };
      jest.spyOn(booksOptionDetailsFormService, 'getBooksOptionDetails').mockReturnValue({ id: null });
      jest.spyOn(booksOptionDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksOptionDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booksOptionDetails }));
      saveSubject.complete();

      // THEN
      expect(booksOptionDetailsFormService.getBooksOptionDetails).toHaveBeenCalled();
      expect(booksOptionDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksOptionDetails>>();
      const booksOptionDetails = { id: 123 };
      jest.spyOn(booksOptionDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksOptionDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(booksOptionDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBooks', () => {
      it('Should forward to booksService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(booksService, 'compareBooks');
        comp.compareBooks(entity, entity2);
        expect(booksService.compareBooks).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
