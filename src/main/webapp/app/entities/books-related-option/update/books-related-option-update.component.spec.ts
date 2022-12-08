import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BooksRelatedOptionFormService } from './books-related-option-form.service';
import { BooksRelatedOptionService } from '../service/books-related-option.service';
import { IBooksRelatedOption } from '../books-related-option.model';
import { IBooksRelatedOptionDetails } from 'app/entities/books-related-option-details/books-related-option-details.model';
import { BooksRelatedOptionDetailsService } from 'app/entities/books-related-option-details/service/books-related-option-details.service';

import { BooksRelatedOptionUpdateComponent } from './books-related-option-update.component';

describe('BooksRelatedOption Management Update Component', () => {
  let comp: BooksRelatedOptionUpdateComponent;
  let fixture: ComponentFixture<BooksRelatedOptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let booksRelatedOptionFormService: BooksRelatedOptionFormService;
  let booksRelatedOptionService: BooksRelatedOptionService;
  let booksRelatedOptionDetailsService: BooksRelatedOptionDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BooksRelatedOptionUpdateComponent],
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
      .overrideTemplate(BooksRelatedOptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BooksRelatedOptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    booksRelatedOptionFormService = TestBed.inject(BooksRelatedOptionFormService);
    booksRelatedOptionService = TestBed.inject(BooksRelatedOptionService);
    booksRelatedOptionDetailsService = TestBed.inject(BooksRelatedOptionDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BooksRelatedOptionDetails query and add missing value', () => {
      const booksRelatedOption: IBooksRelatedOption = { id: 456 };
      const booksRelatedOptionDetails: IBooksRelatedOptionDetails[] = [{ id: 75384 }];
      booksRelatedOption.booksRelatedOptionDetails = booksRelatedOptionDetails;

      const booksRelatedOptionDetailsCollection: IBooksRelatedOptionDetails[] = [{ id: 35392 }];
      jest
        .spyOn(booksRelatedOptionDetailsService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: booksRelatedOptionDetailsCollection })));
      const additionalBooksRelatedOptionDetails = [...booksRelatedOptionDetails];
      const expectedCollection: IBooksRelatedOptionDetails[] = [
        ...additionalBooksRelatedOptionDetails,
        ...booksRelatedOptionDetailsCollection,
      ];
      jest.spyOn(booksRelatedOptionDetailsService, 'addBooksRelatedOptionDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ booksRelatedOption });
      comp.ngOnInit();

      expect(booksRelatedOptionDetailsService.query).toHaveBeenCalled();
      expect(booksRelatedOptionDetailsService.addBooksRelatedOptionDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        booksRelatedOptionDetailsCollection,
        ...additionalBooksRelatedOptionDetails.map(expect.objectContaining)
      );
      expect(comp.booksRelatedOptionDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const booksRelatedOption: IBooksRelatedOption = { id: 456 };
      const booksRelatedOptionDetails: IBooksRelatedOptionDetails = { id: 48335 };
      booksRelatedOption.booksRelatedOptionDetails = [booksRelatedOptionDetails];

      activatedRoute.data = of({ booksRelatedOption });
      comp.ngOnInit();

      expect(comp.booksRelatedOptionDetailsSharedCollection).toContain(booksRelatedOptionDetails);
      expect(comp.booksRelatedOption).toEqual(booksRelatedOption);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksRelatedOption>>();
      const booksRelatedOption = { id: 123 };
      jest.spyOn(booksRelatedOptionFormService, 'getBooksRelatedOption').mockReturnValue(booksRelatedOption);
      jest.spyOn(booksRelatedOptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksRelatedOption });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booksRelatedOption }));
      saveSubject.complete();

      // THEN
      expect(booksRelatedOptionFormService.getBooksRelatedOption).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(booksRelatedOptionService.update).toHaveBeenCalledWith(expect.objectContaining(booksRelatedOption));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksRelatedOption>>();
      const booksRelatedOption = { id: 123 };
      jest.spyOn(booksRelatedOptionFormService, 'getBooksRelatedOption').mockReturnValue({ id: null });
      jest.spyOn(booksRelatedOptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksRelatedOption: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booksRelatedOption }));
      saveSubject.complete();

      // THEN
      expect(booksRelatedOptionFormService.getBooksRelatedOption).toHaveBeenCalled();
      expect(booksRelatedOptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksRelatedOption>>();
      const booksRelatedOption = { id: 123 };
      jest.spyOn(booksRelatedOptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksRelatedOption });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(booksRelatedOptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBooksRelatedOptionDetails', () => {
      it('Should forward to booksRelatedOptionDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(booksRelatedOptionDetailsService, 'compareBooksRelatedOptionDetails');
        comp.compareBooksRelatedOptionDetails(entity, entity2);
        expect(booksRelatedOptionDetailsService.compareBooksRelatedOptionDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
