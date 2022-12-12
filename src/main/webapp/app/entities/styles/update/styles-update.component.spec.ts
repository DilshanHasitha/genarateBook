import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StylesFormService } from './styles-form.service';
import { StylesService } from '../service/styles.service';
import { IStyles } from '../styles.model';
import { IStylesDetails } from 'app/entities/styles-details/styles-details.model';
import { StylesDetailsService } from 'app/entities/styles-details/service/styles-details.service';

import { StylesUpdateComponent } from './styles-update.component';

describe('Styles Management Update Component', () => {
  let comp: StylesUpdateComponent;
  let fixture: ComponentFixture<StylesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stylesFormService: StylesFormService;
  let stylesService: StylesService;
  let stylesDetailsService: StylesDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [StylesUpdateComponent],
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
      .overrideTemplate(StylesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StylesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stylesFormService = TestBed.inject(StylesFormService);
    stylesService = TestBed.inject(StylesService);
    stylesDetailsService = TestBed.inject(StylesDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call StylesDetails query and add missing value', () => {
      const styles: IStyles = { id: 456 };
      const stylesDetails: IStylesDetails[] = [{ id: 37949 }];
      styles.stylesDetails = stylesDetails;

      const stylesDetailsCollection: IStylesDetails[] = [{ id: 73288 }];
      jest.spyOn(stylesDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: stylesDetailsCollection })));
      const additionalStylesDetails = [...stylesDetails];
      const expectedCollection: IStylesDetails[] = [...additionalStylesDetails, ...stylesDetailsCollection];
      jest.spyOn(stylesDetailsService, 'addStylesDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ styles });
      comp.ngOnInit();

      expect(stylesDetailsService.query).toHaveBeenCalled();
      expect(stylesDetailsService.addStylesDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        stylesDetailsCollection,
        ...additionalStylesDetails.map(expect.objectContaining)
      );
      expect(comp.stylesDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const styles: IStyles = { id: 456 };
      const stylesDetails: IStylesDetails = { id: 77397 };
      styles.stylesDetails = [stylesDetails];

      activatedRoute.data = of({ styles });
      comp.ngOnInit();

      expect(comp.stylesDetailsSharedCollection).toContain(stylesDetails);
      expect(comp.styles).toEqual(styles);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStyles>>();
      const styles = { id: 123 };
      jest.spyOn(stylesFormService, 'getStyles').mockReturnValue(styles);
      jest.spyOn(stylesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ styles });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: styles }));
      saveSubject.complete();

      // THEN
      expect(stylesFormService.getStyles).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stylesService.update).toHaveBeenCalledWith(expect.objectContaining(styles));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStyles>>();
      const styles = { id: 123 };
      jest.spyOn(stylesFormService, 'getStyles').mockReturnValue({ id: null });
      jest.spyOn(stylesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ styles: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: styles }));
      saveSubject.complete();

      // THEN
      expect(stylesFormService.getStyles).toHaveBeenCalled();
      expect(stylesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStyles>>();
      const styles = { id: 123 };
      jest.spyOn(stylesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ styles });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stylesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStylesDetails', () => {
      it('Should forward to stylesDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(stylesDetailsService, 'compareStylesDetails');
        comp.compareStylesDetails(entity, entity2);
        expect(stylesDetailsService.compareStylesDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
