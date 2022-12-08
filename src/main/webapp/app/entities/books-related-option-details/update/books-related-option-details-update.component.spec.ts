import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BooksRelatedOptionDetailsFormService } from './books-related-option-details-form.service';
import { BooksRelatedOptionDetailsService } from '../service/books-related-option-details.service';
import { IBooksRelatedOptionDetails } from '../books-related-option-details.model';

import { BooksRelatedOptionDetailsUpdateComponent } from './books-related-option-details-update.component';

describe('BooksRelatedOptionDetails Management Update Component', () => {
  let comp: BooksRelatedOptionDetailsUpdateComponent;
  let fixture: ComponentFixture<BooksRelatedOptionDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let booksRelatedOptionDetailsFormService: BooksRelatedOptionDetailsFormService;
  let booksRelatedOptionDetailsService: BooksRelatedOptionDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BooksRelatedOptionDetailsUpdateComponent],
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
      .overrideTemplate(BooksRelatedOptionDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BooksRelatedOptionDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    booksRelatedOptionDetailsFormService = TestBed.inject(BooksRelatedOptionDetailsFormService);
    booksRelatedOptionDetailsService = TestBed.inject(BooksRelatedOptionDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const booksRelatedOptionDetails: IBooksRelatedOptionDetails = { id: 456 };

      activatedRoute.data = of({ booksRelatedOptionDetails });
      comp.ngOnInit();

      expect(comp.booksRelatedOptionDetails).toEqual(booksRelatedOptionDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksRelatedOptionDetails>>();
      const booksRelatedOptionDetails = { id: 123 };
      jest.spyOn(booksRelatedOptionDetailsFormService, 'getBooksRelatedOptionDetails').mockReturnValue(booksRelatedOptionDetails);
      jest.spyOn(booksRelatedOptionDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksRelatedOptionDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booksRelatedOptionDetails }));
      saveSubject.complete();

      // THEN
      expect(booksRelatedOptionDetailsFormService.getBooksRelatedOptionDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(booksRelatedOptionDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(booksRelatedOptionDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksRelatedOptionDetails>>();
      const booksRelatedOptionDetails = { id: 123 };
      jest.spyOn(booksRelatedOptionDetailsFormService, 'getBooksRelatedOptionDetails').mockReturnValue({ id: null });
      jest.spyOn(booksRelatedOptionDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksRelatedOptionDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booksRelatedOptionDetails }));
      saveSubject.complete();

      // THEN
      expect(booksRelatedOptionDetailsFormService.getBooksRelatedOptionDetails).toHaveBeenCalled();
      expect(booksRelatedOptionDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksRelatedOptionDetails>>();
      const booksRelatedOptionDetails = { id: 123 };
      jest.spyOn(booksRelatedOptionDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksRelatedOptionDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(booksRelatedOptionDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
