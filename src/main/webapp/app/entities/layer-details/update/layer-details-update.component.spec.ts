import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LayerDetailsFormService } from './layer-details-form.service';
import { LayerDetailsService } from '../service/layer-details.service';
import { ILayerDetails } from '../layer-details.model';

import { LayerDetailsUpdateComponent } from './layer-details-update.component';

describe('LayerDetails Management Update Component', () => {
  let comp: LayerDetailsUpdateComponent;
  let fixture: ComponentFixture<LayerDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let layerDetailsFormService: LayerDetailsFormService;
  let layerDetailsService: LayerDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LayerDetailsUpdateComponent],
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
      .overrideTemplate(LayerDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LayerDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    layerDetailsFormService = TestBed.inject(LayerDetailsFormService);
    layerDetailsService = TestBed.inject(LayerDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const layerDetails: ILayerDetails = { id: 456 };

      activatedRoute.data = of({ layerDetails });
      comp.ngOnInit();

      expect(comp.layerDetails).toEqual(layerDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILayerDetails>>();
      const layerDetails = { id: 123 };
      jest.spyOn(layerDetailsFormService, 'getLayerDetails').mockReturnValue(layerDetails);
      jest.spyOn(layerDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ layerDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: layerDetails }));
      saveSubject.complete();

      // THEN
      expect(layerDetailsFormService.getLayerDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(layerDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(layerDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILayerDetails>>();
      const layerDetails = { id: 123 };
      jest.spyOn(layerDetailsFormService, 'getLayerDetails').mockReturnValue({ id: null });
      jest.spyOn(layerDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ layerDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: layerDetails }));
      saveSubject.complete();

      // THEN
      expect(layerDetailsFormService.getLayerDetails).toHaveBeenCalled();
      expect(layerDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILayerDetails>>();
      const layerDetails = { id: 123 };
      jest.spyOn(layerDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ layerDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(layerDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
