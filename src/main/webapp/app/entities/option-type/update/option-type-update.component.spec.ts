import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OptionTypeFormService } from './option-type-form.service';
import { OptionTypeService } from '../service/option-type.service';
import { IOptionType } from '../option-type.model';

import { OptionTypeUpdateComponent } from './option-type-update.component';

describe('OptionType Management Update Component', () => {
  let comp: OptionTypeUpdateComponent;
  let fixture: ComponentFixture<OptionTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let optionTypeFormService: OptionTypeFormService;
  let optionTypeService: OptionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OptionTypeUpdateComponent],
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
      .overrideTemplate(OptionTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OptionTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    optionTypeFormService = TestBed.inject(OptionTypeFormService);
    optionTypeService = TestBed.inject(OptionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const optionType: IOptionType = { id: 456 };

      activatedRoute.data = of({ optionType });
      comp.ngOnInit();

      expect(comp.optionType).toEqual(optionType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOptionType>>();
      const optionType = { id: 123 };
      jest.spyOn(optionTypeFormService, 'getOptionType').mockReturnValue(optionType);
      jest.spyOn(optionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ optionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: optionType }));
      saveSubject.complete();

      // THEN
      expect(optionTypeFormService.getOptionType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(optionTypeService.update).toHaveBeenCalledWith(expect.objectContaining(optionType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOptionType>>();
      const optionType = { id: 123 };
      jest.spyOn(optionTypeFormService, 'getOptionType').mockReturnValue({ id: null });
      jest.spyOn(optionTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ optionType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: optionType }));
      saveSubject.complete();

      // THEN
      expect(optionTypeFormService.getOptionType).toHaveBeenCalled();
      expect(optionTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOptionType>>();
      const optionType = { id: 123 };
      jest.spyOn(optionTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ optionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(optionTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
