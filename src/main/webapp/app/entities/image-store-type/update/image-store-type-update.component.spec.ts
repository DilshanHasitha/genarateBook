import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ImageStoreTypeFormService } from './image-store-type-form.service';
import { ImageStoreTypeService } from '../service/image-store-type.service';
import { IImageStoreType } from '../image-store-type.model';

import { ImageStoreTypeUpdateComponent } from './image-store-type-update.component';

describe('ImageStoreType Management Update Component', () => {
  let comp: ImageStoreTypeUpdateComponent;
  let fixture: ComponentFixture<ImageStoreTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imageStoreTypeFormService: ImageStoreTypeFormService;
  let imageStoreTypeService: ImageStoreTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ImageStoreTypeUpdateComponent],
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
      .overrideTemplate(ImageStoreTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImageStoreTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imageStoreTypeFormService = TestBed.inject(ImageStoreTypeFormService);
    imageStoreTypeService = TestBed.inject(ImageStoreTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const imageStoreType: IImageStoreType = { id: 456 };

      activatedRoute.data = of({ imageStoreType });
      comp.ngOnInit();

      expect(comp.imageStoreType).toEqual(imageStoreType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageStoreType>>();
      const imageStoreType = { id: 123 };
      jest.spyOn(imageStoreTypeFormService, 'getImageStoreType').mockReturnValue(imageStoreType);
      jest.spyOn(imageStoreTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageStoreType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageStoreType }));
      saveSubject.complete();

      // THEN
      expect(imageStoreTypeFormService.getImageStoreType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(imageStoreTypeService.update).toHaveBeenCalledWith(expect.objectContaining(imageStoreType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageStoreType>>();
      const imageStoreType = { id: 123 };
      jest.spyOn(imageStoreTypeFormService, 'getImageStoreType').mockReturnValue({ id: null });
      jest.spyOn(imageStoreTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageStoreType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imageStoreType }));
      saveSubject.complete();

      // THEN
      expect(imageStoreTypeFormService.getImageStoreType).toHaveBeenCalled();
      expect(imageStoreTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImageStoreType>>();
      const imageStoreType = { id: 123 };
      jest.spyOn(imageStoreTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imageStoreType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imageStoreTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
