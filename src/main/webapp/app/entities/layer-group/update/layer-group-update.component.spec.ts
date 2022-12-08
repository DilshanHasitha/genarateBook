import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LayerGroupFormService } from './layer-group-form.service';
import { LayerGroupService } from '../service/layer-group.service';
import { ILayerGroup } from '../layer-group.model';
import { ILayers } from 'app/entities/layers/layers.model';
import { LayersService } from 'app/entities/layers/service/layers.service';

import { LayerGroupUpdateComponent } from './layer-group-update.component';

describe('LayerGroup Management Update Component', () => {
  let comp: LayerGroupUpdateComponent;
  let fixture: ComponentFixture<LayerGroupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let layerGroupFormService: LayerGroupFormService;
  let layerGroupService: LayerGroupService;
  let layersService: LayersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LayerGroupUpdateComponent],
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
      .overrideTemplate(LayerGroupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LayerGroupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    layerGroupFormService = TestBed.inject(LayerGroupFormService);
    layerGroupService = TestBed.inject(LayerGroupService);
    layersService = TestBed.inject(LayersService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Layers query and add missing value', () => {
      const layerGroup: ILayerGroup = { id: 456 };
      const layers: ILayers[] = [{ id: 18480 }];
      layerGroup.layers = layers;

      const layersCollection: ILayers[] = [{ id: 2808 }];
      jest.spyOn(layersService, 'query').mockReturnValue(of(new HttpResponse({ body: layersCollection })));
      const additionalLayers = [...layers];
      const expectedCollection: ILayers[] = [...additionalLayers, ...layersCollection];
      jest.spyOn(layersService, 'addLayersToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ layerGroup });
      comp.ngOnInit();

      expect(layersService.query).toHaveBeenCalled();
      expect(layersService.addLayersToCollectionIfMissing).toHaveBeenCalledWith(
        layersCollection,
        ...additionalLayers.map(expect.objectContaining)
      );
      expect(comp.layersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const layerGroup: ILayerGroup = { id: 456 };
      const layers: ILayers = { id: 85014 };
      layerGroup.layers = [layers];

      activatedRoute.data = of({ layerGroup });
      comp.ngOnInit();

      expect(comp.layersSharedCollection).toContain(layers);
      expect(comp.layerGroup).toEqual(layerGroup);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILayerGroup>>();
      const layerGroup = { id: 123 };
      jest.spyOn(layerGroupFormService, 'getLayerGroup').mockReturnValue(layerGroup);
      jest.spyOn(layerGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ layerGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: layerGroup }));
      saveSubject.complete();

      // THEN
      expect(layerGroupFormService.getLayerGroup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(layerGroupService.update).toHaveBeenCalledWith(expect.objectContaining(layerGroup));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILayerGroup>>();
      const layerGroup = { id: 123 };
      jest.spyOn(layerGroupFormService, 'getLayerGroup').mockReturnValue({ id: null });
      jest.spyOn(layerGroupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ layerGroup: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: layerGroup }));
      saveSubject.complete();

      // THEN
      expect(layerGroupFormService.getLayerGroup).toHaveBeenCalled();
      expect(layerGroupService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILayerGroup>>();
      const layerGroup = { id: 123 };
      jest.spyOn(layerGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ layerGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(layerGroupService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLayers', () => {
      it('Should forward to layersService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(layersService, 'compareLayers');
        comp.compareLayers(entity, entity2);
        expect(layersService.compareLayers).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
