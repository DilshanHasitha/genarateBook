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

import { StylesUpdateComponent } from './styles-update.component';

describe('Styles Management Update Component', () => {
  let comp: StylesUpdateComponent;
  let fixture: ComponentFixture<StylesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stylesFormService: StylesFormService;
  let stylesService: StylesService;

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const styles: IStyles = { id: 456 };

      activatedRoute.data = of({ styles });
      comp.ngOnInit();

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
});
