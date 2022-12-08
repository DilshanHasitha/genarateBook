import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LayerGroupFormService, LayerGroupFormGroup } from './layer-group-form.service';
import { ILayerGroup } from '../layer-group.model';
import { LayerGroupService } from '../service/layer-group.service';
import { ILayers } from 'app/entities/layers/layers.model';
import { LayersService } from 'app/entities/layers/service/layers.service';

@Component({
  selector: 'jhi-layer-group-update',
  templateUrl: './layer-group-update.component.html',
})
export class LayerGroupUpdateComponent implements OnInit {
  isSaving = false;
  layerGroup: ILayerGroup | null = null;

  layersSharedCollection: ILayers[] = [];

  editForm: LayerGroupFormGroup = this.layerGroupFormService.createLayerGroupFormGroup();

  constructor(
    protected layerGroupService: LayerGroupService,
    protected layerGroupFormService: LayerGroupFormService,
    protected layersService: LayersService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareLayers = (o1: ILayers | null, o2: ILayers | null): boolean => this.layersService.compareLayers(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ layerGroup }) => {
      this.layerGroup = layerGroup;
      if (layerGroup) {
        this.updateForm(layerGroup);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const layerGroup = this.layerGroupFormService.getLayerGroup(this.editForm);
    if (layerGroup.id !== null) {
      this.subscribeToSaveResponse(this.layerGroupService.update(layerGroup));
    } else {
      this.subscribeToSaveResponse(this.layerGroupService.create(layerGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILayerGroup>>): void {
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

  protected updateForm(layerGroup: ILayerGroup): void {
    this.layerGroup = layerGroup;
    this.layerGroupFormService.resetForm(this.editForm, layerGroup);

    this.layersSharedCollection = this.layersService.addLayersToCollectionIfMissing<ILayers>(
      this.layersSharedCollection,
      ...(layerGroup.layers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.layersService
      .query()
      .pipe(map((res: HttpResponse<ILayers[]>) => res.body ?? []))
      .pipe(
        map((layers: ILayers[]) => this.layersService.addLayersToCollectionIfMissing<ILayers>(layers, ...(this.layerGroup?.layers ?? [])))
      )
      .subscribe((layers: ILayers[]) => (this.layersSharedCollection = layers));
  }
  exportPDF(): void {
    this.layerGroupService.exportPDF().subscribe((response: any) => {
      const file = new Blob([response], { type: 'application/pdf' });
      alert;
      const fileURL = URL.createObjectURL(file);
      window.open(fileURL);
    });
  }
}
