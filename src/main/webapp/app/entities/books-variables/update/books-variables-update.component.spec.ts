import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BooksVariablesFormService } from './books-variables-form.service';
import { BooksVariablesService } from '../service/books-variables.service';
import { IBooksVariables } from '../books-variables.model';

import { BooksVariablesUpdateComponent } from './books-variables-update.component';

describe('BooksVariables Management Update Component', () => {
  let comp: BooksVariablesUpdateComponent;
  let fixture: ComponentFixture<BooksVariablesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let booksVariablesFormService: BooksVariablesFormService;
  let booksVariablesService: BooksVariablesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BooksVariablesUpdateComponent],
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
      .overrideTemplate(BooksVariablesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BooksVariablesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    booksVariablesFormService = TestBed.inject(BooksVariablesFormService);
    booksVariablesService = TestBed.inject(BooksVariablesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const booksVariables: IBooksVariables = { id: 456 };

      activatedRoute.data = of({ booksVariables });
      comp.ngOnInit();

      expect(comp.booksVariables).toEqual(booksVariables);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksVariables>>();
      const booksVariables = { id: 123 };
      jest.spyOn(booksVariablesFormService, 'getBooksVariables').mockReturnValue(booksVariables);
      jest.spyOn(booksVariablesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksVariables });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booksVariables }));
      saveSubject.complete();

      // THEN
      expect(booksVariablesFormService.getBooksVariables).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(booksVariablesService.update).toHaveBeenCalledWith(expect.objectContaining(booksVariables));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksVariables>>();
      const booksVariables = { id: 123 };
      jest.spyOn(booksVariablesFormService, 'getBooksVariables').mockReturnValue({ id: null });
      jest.spyOn(booksVariablesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksVariables: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booksVariables }));
      saveSubject.complete();

      // THEN
      expect(booksVariablesFormService.getBooksVariables).toHaveBeenCalled();
      expect(booksVariablesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBooksVariables>>();
      const booksVariables = { id: 123 };
      jest.spyOn(booksVariablesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booksVariables });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(booksVariablesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
