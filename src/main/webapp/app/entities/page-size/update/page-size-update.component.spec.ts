import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PageSizeFormService } from './page-size-form.service';
import { PageSizeService } from '../service/page-size.service';
import { IPageSize } from '../page-size.model';

import { PageSizeUpdateComponent } from './page-size-update.component';

describe('PageSize Management Update Component', () => {
  let comp: PageSizeUpdateComponent;
  let fixture: ComponentFixture<PageSizeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pageSizeFormService: PageSizeFormService;
  let pageSizeService: PageSizeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PageSizeUpdateComponent],
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
      .overrideTemplate(PageSizeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PageSizeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pageSizeFormService = TestBed.inject(PageSizeFormService);
    pageSizeService = TestBed.inject(PageSizeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pageSize: IPageSize = { id: 456 };

      activatedRoute.data = of({ pageSize });
      comp.ngOnInit();

      expect(comp.pageSize).toEqual(pageSize);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageSize>>();
      const pageSize = { id: 123 };
      jest.spyOn(pageSizeFormService, 'getPageSize').mockReturnValue(pageSize);
      jest.spyOn(pageSizeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageSize });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pageSize }));
      saveSubject.complete();

      // THEN
      expect(pageSizeFormService.getPageSize).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pageSizeService.update).toHaveBeenCalledWith(expect.objectContaining(pageSize));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageSize>>();
      const pageSize = { id: 123 };
      jest.spyOn(pageSizeFormService, 'getPageSize').mockReturnValue({ id: null });
      jest.spyOn(pageSizeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageSize: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pageSize }));
      saveSubject.complete();

      // THEN
      expect(pageSizeFormService.getPageSize).toHaveBeenCalled();
      expect(pageSizeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageSize>>();
      const pageSize = { id: 123 };
      jest.spyOn(pageSizeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageSize });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pageSizeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
