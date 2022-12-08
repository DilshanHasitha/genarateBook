import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BooksAttributesFormService } from './books-attributes-form.service';
import { BooksAttributesService } from '../service/books-attributes.service';
import { IBooksAttributes } from '../books-attributes.model';

import { BooksAttributesUpdateComponent } from './books-attributes-update.component';

describe('BooksAttributes Management Update Component', () => {
  let comp: BooksAttributesUpdateComponent;
  let fixture: ComponentFixture<BooksAttributesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let booksAttributesFormService: BooksAttributesFormService;
  let booksAttributesService: BooksAttributesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BooksAttributesUpdateComponent],
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
      .overrideTemplate(BooksAttributesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BooksAttributesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    booksAttributesFormService = TestBed.inject(BooksAttributesFormService);
    booksAttributesService = TestBed.inject(BooksAttributesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const booksAttributes: IBooksAttributes = { id: 456 };

      activatedRoute.data = of({ booksAttributes });
      comp.ngOnInit();

      expect(comp.booksAttributes).toEqual(booksAttributes);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksAttributes>>();
      const booksAttributes = { id: 123 };
      jest.spyOn(booksAttributesFormService, 'getBooksAttributes').mockReturnValue(booksAttributes);
      jest.spyOn(booksAttributesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksAttributes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booksAttributes }));
      saveSubject.complete();

      // THEN
      expect(booksAttributesFormService.getBooksAttributes).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(booksAttributesService.update).toHaveBeenCalledWith(expect.objectContaining(booksAttributes));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksAttributes>>();
      const booksAttributes = { id: 123 };
      jest.spyOn(booksAttributesFormService, 'getBooksAttributes').mockReturnValue({ id: null });
      jest.spyOn(booksAttributesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksAttributes: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booksAttributes }));
      saveSubject.complete();

      // THEN
      expect(booksAttributesFormService.getBooksAttributes).toHaveBeenCalled();
      expect(booksAttributesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksAttributes>>();
      const booksAttributes = { id: 123 };
      jest.spyOn(booksAttributesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksAttributes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(booksAttributesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
