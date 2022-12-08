import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LayersFormService, LayersFormGroup } from './layers-form.service';
import { ILayers } from '../layers.model';
import { LayersService } from '../service/layers.service';
import { ILayerDetails } from 'app/entities/layer-details/layer-details.model';
import { LayerDetailsService } from 'app/entities/layer-details/service/layer-details.service';

@Component({
  selector: 'jhi-layers-update',
  templateUrl: './layers-update.component.html',
})
export class LayersUpdateComponent implements OnInit {
  isSaving = false;
  layers: ILayers | null = null;

  layerDetailsSharedCollection: ILayerDetails[] = [];

  editForm: LayersFormGroup = this.layersFormService.createLayersFormGroup();

  constructor(
    protected layersService: LayersService,
    protected layersFormService: LayersFormService,
    protected layerDetailsService: LayerDetailsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareLayerDetails = (o1: ILayerDetails | null, o2: ILayerDetails | null): boolean =>
    this.layerDetailsService.compareLayerDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ layers }) => {
      this.layers = layers;
      if (layers) {
        this.updateForm(layers);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const layers = this.layersFormService.getLayers(this.editForm);
    if (layers.id !== null) {
      this.subscribeToSaveResponse(this.layersService.update(layers));
    } else {
      this.subscribeToSaveResponse(this.layersService.create(layers));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILayers>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(layers: ILayers): void {
    this.layers = layers;
    this.layersFormService.resetForm(this.editForm, layers);

    this.layerDetailsSharedCollection = this.layerDetailsService.addLayerDetailsToCollectionIfMissing<ILayerDetails>(
      this.layerDetailsSharedCollection,
      ...(layers.layerdetails ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.layerDetailsService
      .query()
      .pipe(map((res: HttpResponse<ILayerDetails[]>) => res.body ?? []))
      .pipe(
        map((layerDetails: ILayerDetails[]) =>
          this.layerDetailsService.addLayerDetailsToCollectionIfMissing<ILayerDetails>(layerDetails, ...(this.layers?.layerdetails ?? []))
        )
      )
      .subscribe((layerDetails: ILayerDetails[]) => (this.layerDetailsSharedCollection = layerDetails));
  }
}
