import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PriceRelatedOptionDetailsFormService, PriceRelatedOptionDetailsFormGroup } from './price-related-option-details-form.service';
import { IPriceRelatedOptionDetails } from '../price-related-option-details.model';
import { PriceRelatedOptionDetailsService } from '../service/price-related-option-details.service';

@Component({
  selector: 'jhi-price-related-option-details-update',
  templateUrl: './price-related-option-details-update.component.html',
})
export class PriceRelatedOptionDetailsUpdateComponent implements OnInit {
  isSaving = false;
  priceRelatedOptionDetails: IPriceRelatedOptionDetails | null = null;

  editForm: PriceRelatedOptionDetailsFormGroup = this.priceRelatedOptionDetailsFormService.createPriceRelatedOptionDetailsFormGroup();

  constructor(
    protected priceRelatedOptionDetailsService: PriceRelatedOptionDetailsService,
    protected priceRelatedOptionDetailsFormService: PriceRelatedOptionDetailsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priceRelatedOptionDetails }) => {
      this.priceRelatedOptionDetails = priceRelatedOptionDetails;
      if (priceRelatedOptionDetails) {
        this.updateForm(priceRelatedOptionDetails);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const priceRelatedOptionDetails = this.priceRelatedOptionDetailsFormService.getPriceRelatedOptionDetails(this.editForm);
    if (priceRelatedOptionDetails.id !== null) {
      this.subscribeToSaveResponse(this.priceRelatedOptionDetailsService.update(priceRelatedOptionDetails));
    } else {
      this.subscribeToSaveResponse(this.priceRelatedOptionDetailsService.create(priceRelatedOptionDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPriceRelatedOptionDetails>>): void {
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

  protected updateForm(priceRelatedOptionDetails: IPriceRelatedOptionDetails): void {
    this.priceRelatedOptionDetails = priceRelatedOptionDetails;
    this.priceRelatedOptionDetailsFormService.resetForm(this.editForm, priceRelatedOptionDetails);
  }
}
