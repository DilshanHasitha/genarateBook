import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PageLayersDetailsFormService } from './page-layers-details-form.service';
import { PageLayersDetailsService } from '../service/page-layers-details.service';
import { IPageLayersDetails } from '../page-layers-details.model';

import { PageLayersDetailsUpdateComponent } from './page-layers-details-update.component';

describe('PageLayersDetails Management Update Component', () => {
  let comp: PageLayersDetailsUpdateComponent;
  let fixture: ComponentFixture<PageLayersDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pageLayersDetailsFormService: PageLayersDetailsFormService;
  let pageLayersDetailsService: PageLayersDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PageLayersDetailsUpdateComponent],
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
      .overrideTemplate(PageLayersDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PageLayersDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pageLayersDetailsFormService = TestBed.inject(PageLayersDetailsFormService);
    pageLayersDetailsService = TestBed.inject(PageLayersDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pageLayersDetails: IPageLayersDetails = { id: 456 };

      activatedRoute.data = of({ pageLayersDetails });
      comp.ngOnInit();

      expect(comp.pageLayersDetails).toEqual(pageLayersDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageLayersDetails>>();
      const pageLayersDetails = { id: 123 };
      jest.spyOn(pageLayersDetailsFormService, 'getPageLayersDetails').mockReturnValue(pageLayersDetails);
      jest.spyOn(pageLayersDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageLayersDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pageLayersDetails }));
      saveSubject.complete();

      // THEN
      expect(pageLayersDetailsFormService.getPageLayersDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pageLayersDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(pageLayersDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageLayersDetails>>();
      const pageLayersDetails = { id: 123 };
      jest.spyOn(pageLayersDetailsFormService, 'getPageLayersDetails').mockReturnValue({ id: null });
      jest.spyOn(pageLayersDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageLayersDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pageLayersDetails }));
      saveSubject.complete();

      // THEN
      expect(pageLayersDetailsFormService.getPageLayersDetails).toHaveBeenCalled();
      expect(pageLayersDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageLayersDetails>>();
      const pageLayersDetails = { id: 123 };
      jest.spyOn(pageLayersDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageLayersDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pageLayersDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
