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
import { IStyles } from 'app/entities/styles/styles.model';
import { StylesService } from 'app/entities/styles/service/styles.service';

import { OptionsUpdateComponent } from './options-update.component';

describe('Options Management Update Component', () => {
  let comp: OptionsUpdateComponent;
  let fixture: ComponentFixture<OptionsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let optionsFormService: OptionsFormService;
  let optionsService: OptionsService;
  let stylesService: StylesService;

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
    stylesService = TestBed.inject(StylesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Styles query and add missing value', () => {
      const options: IOptions = { id: 456 };
      const styles: IStyles[] = [{ id: 792 }];
      options.styles = styles;

      const stylesCollection: IStyles[] = [{ id: 18963 }];
      jest.spyOn(stylesService, 'query').mockReturnValue(of(new HttpResponse({ body: stylesCollection })));
      const additionalStyles = [...styles];
      const expectedCollection: IStyles[] = [...additionalStyles, ...stylesCollection];
      jest.spyOn(stylesService, 'addStylesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ options });
      comp.ngOnInit();

      expect(stylesService.query).toHaveBeenCalled();
      expect(stylesService.addStylesToCollectionIfMissing).toHaveBeenCalledWith(
        stylesCollection,
        ...additionalStyles.map(expect.objectContaining)
      );
      expect(comp.stylesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const options: IOptions = { id: 456 };
      const style: IStyles = { id: 60979 };
      options.styles = [style];

      activatedRoute.data = of({ options });
      comp.ngOnInit();

      expect(comp.stylesSharedCollection).toContain(style);
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

  describe('Compare relationships', () => {
    describe('compareStyles', () => {
      it('Should forward to stylesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(stylesService, 'compareStyles');
        comp.compareStyles(entity, entity2);
        expect(stylesService.compareStyles).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
