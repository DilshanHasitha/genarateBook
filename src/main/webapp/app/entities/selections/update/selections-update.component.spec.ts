import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SelectionsFormService } from './selections-form.service';
import { SelectionsService } from '../service/selections.service';
import { ISelections } from '../selections.model';

import { SelectionsUpdateComponent } from './selections-update.component';

describe('Selections Management Update Component', () => {
  let comp: SelectionsUpdateComponent;
  let fixture: ComponentFixture<SelectionsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let selectionsFormService: SelectionsFormService;
  let selectionsService: SelectionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SelectionsUpdateComponent],
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
      .overrideTemplate(SelectionsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SelectionsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    selectionsFormService = TestBed.inject(SelectionsFormService);
    selectionsService = TestBed.inject(SelectionsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const selections: ISelections = { id: 456 };

      activatedRoute.data = of({ selections });
      comp.ngOnInit();

      expect(comp.selections).toEqual(selections);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISelections>>();
      const selections = { id: 123 };
      jest.spyOn(selectionsFormService, 'getSelections').mockReturnValue(selections);
      jest.spyOn(selectionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ selections });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: selections }));
      saveSubject.complete();

      // THEN
      expect(selectionsFormService.getSelections).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(selectionsService.update).toHaveBeenCalledWith(expect.objectContaining(selections));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISelections>>();
      const selections = { id: 123 };
      jest.spyOn(selectionsFormService, 'getSelections').mockReturnValue({ id: null });
      jest.spyOn(selectionsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ selections: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: selections }));
      saveSubject.complete();

      // THEN
      expect(selectionsFormService.getSelections).toHaveBeenCalled();
      expect(selectionsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISelections>>();
      const selections = { id: 123 };
      jest.spyOn(selectionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ selections });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(selectionsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
