import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FontFamilyFormService } from './font-family-form.service';
import { FontFamilyService } from '../service/font-family.service';
import { IFontFamily } from '../font-family.model';

import { FontFamilyUpdateComponent } from './font-family-update.component';

describe('FontFamily Management Update Component', () => {
  let comp: FontFamilyUpdateComponent;
  let fixture: ComponentFixture<FontFamilyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fontFamilyFormService: FontFamilyFormService;
  let fontFamilyService: FontFamilyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FontFamilyUpdateComponent],
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
      .overrideTemplate(FontFamilyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FontFamilyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fontFamilyFormService = TestBed.inject(FontFamilyFormService);
    fontFamilyService = TestBed.inject(FontFamilyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fontFamily: IFontFamily = { id: 456 };

      activatedRoute.data = of({ fontFamily });
      comp.ngOnInit();

      expect(comp.fontFamily).toEqual(fontFamily);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFontFamily>>();
      const fontFamily = { id: 123 };
      jest.spyOn(fontFamilyFormService, 'getFontFamily').mockReturnValue(fontFamily);
      jest.spyOn(fontFamilyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fontFamily });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fontFamily }));
      saveSubject.complete();

      // THEN
      expect(fontFamilyFormService.getFontFamily).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fontFamilyService.update).toHaveBeenCalledWith(expect.objectContaining(fontFamily));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFontFamily>>();
      const fontFamily = { id: 123 };
      jest.spyOn(fontFamilyFormService, 'getFontFamily').mockReturnValue({ id: null });
      jest.spyOn(fontFamilyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fontFamily: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fontFamily }));
      saveSubject.complete();

      // THEN
      expect(fontFamilyFormService.getFontFamily).toHaveBeenCalled();
      expect(fontFamilyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFontFamily>>();
      const fontFamily = { id: 123 };
      jest.spyOn(fontFamilyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fontFamily });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fontFamilyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
