import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PageLayersFormService } from './page-layers-form.service';
import { PageLayersService } from '../service/page-layers.service';
import { IPageLayers } from '../page-layers.model';
import { IPageLayersDetails } from 'app/entities/page-layers-details/page-layers-details.model';
import { PageLayersDetailsService } from 'app/entities/page-layers-details/service/page-layers-details.service';

import { PageLayersUpdateComponent } from './page-layers-update.component';

describe('PageLayers Management Update Component', () => {
  let comp: PageLayersUpdateComponent;
  let fixture: ComponentFixture<PageLayersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pageLayersFormService: PageLayersFormService;
  let pageLayersService: PageLayersService;
  let pageLayersDetailsService: PageLayersDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PageLayersUpdateComponent],
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
      .overrideTemplate(PageLayersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PageLayersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pageLayersFormService = TestBed.inject(PageLayersFormService);
    pageLayersService = TestBed.inject(PageLayersService);
    pageLayersDetailsService = TestBed.inject(PageLayersDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PageLayersDetails query and add missing value', () => {
      const pageLayers: IPageLayers = { id: 456 };
      const pageElementDetails: IPageLayersDetails[] = [{ id: 52733 }];
      pageLayers.pageElementDetails = pageElementDetails;

      const pageLayersDetailsCollection: IPageLayersDetails[] = [{ id: 25850 }];
      jest.spyOn(pageLayersDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: pageLayersDetailsCollection })));
      const additionalPageLayersDetails = [...pageElementDetails];
      const expectedCollection: IPageLayersDetails[] = [...additionalPageLayersDetails, ...pageLayersDetailsCollection];
      jest.spyOn(pageLayersDetailsService, 'addPageLayersDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pageLayers });
      comp.ngOnInit();

      expect(pageLayersDetailsService.query).toHaveBeenCalled();
      expect(pageLayersDetailsService.addPageLayersDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        pageLayersDetailsCollection,
        ...additionalPageLayersDetails.map(expect.objectContaining)
      );
      expect(comp.pageLayersDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pageLayers: IPageLayers = { id: 456 };
      const pageElementDetails: IPageLayersDetails = { id: 93421 };
      pageLayers.pageElementDetails = [pageElementDetails];

      activatedRoute.data = of({ pageLayers });
      comp.ngOnInit();

      expect(comp.pageLayersDetailsSharedCollection).toContain(pageElementDetails);
      expect(comp.pageLayers).toEqual(pageLayers);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageLayers>>();
      const pageLayers = { id: 123 };
      jest.spyOn(pageLayersFormService, 'getPageLayers').mockReturnValue(pageLayers);
      jest.spyOn(pageLayersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageLayers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pageLayers }));
      saveSubject.complete();

      // THEN
      expect(pageLayersFormService.getPageLayers).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pageLayersService.update).toHaveBeenCalledWith(expect.objectContaining(pageLayers));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageLayers>>();
      const pageLayers = { id: 123 };
      jest.spyOn(pageLayersFormService, 'getPageLayers').mockReturnValue({ id: null });
      jest.spyOn(pageLayersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageLayers: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pageLayers }));
      saveSubject.complete();

      // THEN
      expect(pageLayersFormService.getPageLayers).toHaveBeenCalled();
      expect(pageLayersService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageLayers>>();
      const pageLayers = { id: 123 };
      jest.spyOn(pageLayersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageLayers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pageLayersService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePageLayersDetails', () => {
      it('Should forward to pageLayersDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pageLayersDetailsService, 'comparePageLayersDetails');
        comp.comparePageLayersDetails(entity, entity2);
        expect(pageLayersDetailsService.comparePageLayersDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
