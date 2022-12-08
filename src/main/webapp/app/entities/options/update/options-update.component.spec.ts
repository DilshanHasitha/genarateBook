import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OptionsFormService } from './options-form.service';
import { OptionsService } from '../service/options.service';
import { IOptions } from '../options.model';

import { OptionsUpdateComponent } from './options-update.component';

describe('Options Management Update Component', () => {
  let comp: OptionsUpdateComponent;
  let fixture: ComponentFixture<OptionsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let optionsFormService: OptionsFormService;
  let optionsService: OptionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OptionsUpdateComponent],
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
      .overrideTemplate(OptionsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OptionsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    optionsFormService = TestBed.inject(OptionsFormService);
    optionsService = TestBed.inject(OptionsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const options: IOptions = { id: 456 };

      activatedRoute.data = of({ options });
      comp.ngOnInit();

      expect(comp.options).toEqual(options);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOptions>>();
      const options = { id: 123 };
      jest.spyOn(optionsFormService, 'getOptions').mockReturnValue(options);
      jest.spyOn(optionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ options });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: options }));
      saveSubject.complete();

      // THEN
      expect(optionsFormService.getOptions).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(optionsService.update).toHaveBeenCalledWith(expect.objectContaining(options));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOptions>>();
      const options = { id: 123 };
      jest.spyOn(optionsFormService, 'getOptions').mockReturnValue({ id: null });
      jest.spyOn(optionsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ options: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: options }));
      saveSubject.complete();

      // THEN
      expect(optionsFormService.getOptions).toHaveBeenCalled();
      expect(optionsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOptions>>();
      const options = { id: 123 };
      jest.spyOn(optionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ options });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(optionsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
