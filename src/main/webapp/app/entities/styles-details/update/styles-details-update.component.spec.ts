import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StylesDetailsFormService } from './styles-details-form.service';
import { StylesDetailsService } from '../service/styles-details.service';
import { IStylesDetails } from '../styles-details.model';

import { StylesDetailsUpdateComponent } from './styles-details-update.component';

describe('StylesDetails Management Update Component', () => {
  let comp: StylesDetailsUpdateComponent;
  let fixture: ComponentFixture<StylesDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stylesDetailsFormService: StylesDetailsFormService;
  let stylesDetailsService: StylesDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [StylesDetailsUpdateComponent],
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
      .overrideTemplate(StylesDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StylesDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stylesDetailsFormService = TestBed.inject(StylesDetailsFormService);
    stylesDetailsService = TestBed.inject(StylesDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const stylesDetails: IStylesDetails = { id: 456 };

      activatedRoute.data = of({ stylesDetails });
      comp.ngOnInit();

      expect(comp.stylesDetails).toEqual(stylesDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStylesDetails>>();
      const stylesDetails = { id: 123 };
      jest.spyOn(stylesDetailsFormService, 'getStylesDetails').mockReturnValue(stylesDetails);
      jest.spyOn(stylesDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stylesDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stylesDetails }));
      saveSubject.complete();

      // THEN
      expect(stylesDetailsFormService.getStylesDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stylesDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(stylesDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStylesDetails>>();
      const stylesDetails = { id: 123 };
      jest.spyOn(stylesDetailsFormService, 'getStylesDetails').mockReturnValue({ id: null });
      jest.spyOn(stylesDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stylesDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stylesDetails }));
      saveSubject.complete();

      // THEN
      expect(stylesDetailsFormService.getStylesDetails).toHaveBeenCalled();
      expect(stylesDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStylesDetails>>();
      const stylesDetails = { id: 123 };
      jest.spyOn(stylesDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stylesDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stylesDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
