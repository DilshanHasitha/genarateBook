import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { LayerDetailsFormService, LayerDetailsFormGroup } from './layer-details-form.service';
import { ILayerDetails } from '../layer-details.model';
import { LayerDetailsService } from '../service/layer-details.service';

@Component({
  selector: 'jhi-layer-details-update',
  templateUrl: './layer-details-update.component.html',
})
export class LayerDetailsUpdateComponent implements OnInit {
  isSaving = false;
  layerDetails: ILayerDetails | null = null;

  editForm: LayerDetailsFormGroup = this.layerDetailsFormService.createLayerDetailsFormGroup();

  constructor(
    protected layerDetailsService: LayerDetailsService,
    protected layerDetailsFormService: LayerDetailsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ layerDetails }) => {
      this.layerDetails = layerDetails;
      if (layerDetails) {
        this.updateForm(layerDetails);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const layerDetails = this.layerDetailsFormService.getLayerDetails(this.editForm);
    if (layerDetails.id !== null) {
      this.subscribeToSaveResponse(this.layerDetailsService.update(layerDetails));
    } else {
      this.subscribeToSaveResponse(this.layerDetailsService.create(layerDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILayerDetails>>): void {
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

  protected updateForm(layerDetails: ILayerDetails): void {
    this.layerDetails = layerDetails;
    this.layerDetailsFormService.resetForm(this.editForm, layerDetails);
  }
}
