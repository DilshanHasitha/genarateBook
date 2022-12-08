import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LayersFormService } from './layers-form.service';
import { LayersService } from '../service/layers.service';
import { ILayers } from '../layers.model';
import { ILayerDetails } from 'app/entities/layer-details/layer-details.model';
import { LayerDetailsService } from 'app/entities/layer-details/service/layer-details.service';

import { LayersUpdateComponent } from './layers-update.component';

describe('Layers Management Update Component', () => {
  let comp: LayersUpdateComponent;
  let fixture: ComponentFixture<LayersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let layersFormService: LayersFormService;
  let layersService: LayersService;
  let layerDetailsService: LayerDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LayersUpdateComponent],
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
      .overrideTemplate(LayersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LayersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    layersFormService = TestBed.inject(LayersFormService);
    layersService = TestBed.inject(LayersService);
    layerDetailsService = TestBed.inject(LayerDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call LayerDetails query and add missing value', () => {
      const layers: ILayers = { id: 456 };
      const layerdetails: ILayerDetails[] = [{ id: 74646 }];
      layers.layerdetails = layerdetails;

      const layerDetailsCollection: ILayerDetails[] = [{ id: 42305 }];
      jest.spyOn(layerDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: layerDetailsCollection })));
      const additionalLayerDetails = [...layerdetails];
      const expectedCollection: ILayerDetails[] = [...additionalLayerDetails, ...layerDetailsCollection];
      jest.spyOn(layerDetailsService, 'addLayerDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ layers });
      comp.ngOnInit();

      expect(layerDetailsService.query).toHaveBeenCalled();
      expect(layerDetailsService.addLayerDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        layerDetailsCollection,
        ...additionalLayerDetails.map(expect.objectContaining)
      );
      expect(comp.layerDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const layers: ILayers = { id: 456 };
      const layerdetails: ILayerDetails = { id: 41876 };
      layers.layerdetails = [layerdetails];

      activatedRoute.data = of({ layers });
      comp.ngOnInit();

      expect(comp.layerDetailsSharedCollection).toContain(layerdetails);
      expect(comp.layers).toEqual(layers);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILayers>>();
      const layers = { id: 123 };
      jest.spyOn(layersFormService, 'getLayers').mockReturnValue(layers);
      jest.spyOn(layersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ layers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: layers }));
      saveSubject.complete();

      // THEN
      expect(layersFormService.getLayers).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(layersService.update).toHaveBeenCalledWith(expect.objectContaining(layers));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILayers>>();
      const layers = { id: 123 };
      jest.spyOn(layersFormService, 'getLayers').mockReturnValue({ id: null });
      jest.spyOn(layersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ layers: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: layers }));
      saveSubject.complete();

      // THEN
      expect(layersFormService.getLayers).toHaveBeenCalled();
      expect(layersService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILayers>>();
      const layers = { id: 123 };
      jest.spyOn(layersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ layers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(layersService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLayerDetails', () => {
      it('Should forward to layerDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(layerDetailsService, 'compareLayerDetails');
        comp.compareLayerDetails(entity, entity2);
        expect(layerDetailsService.compareLayerDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
