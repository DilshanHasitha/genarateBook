import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PriceRelatedOptionFormService, PriceRelatedOptionFormGroup } from './price-related-option-form.service';
import { IPriceRelatedOption } from '../price-related-option.model';
import { PriceRelatedOptionService } from '../service/price-related-option.service';
import { IOptionType } from 'app/entities/option-type/option-type.model';
import { OptionTypeService } from 'app/entities/option-type/service/option-type.service';
import { IPriceRelatedOptionDetails } from 'app/entities/price-related-option-details/price-related-option-details.model';
import { PriceRelatedOptionDetailsService } from 'app/entities/price-related-option-details/service/price-related-option-details.service';

@Component({
  selector: 'jhi-price-related-option-update',
  templateUrl: './price-related-option-update.component.html',
})
export class PriceRelatedOptionUpdateComponent implements OnInit {
  isSaving = false;
  priceRelatedOption: IPriceRelatedOption | null = null;

  optionTypesSharedCollection: IOptionType[] = [];
  priceRelatedOptionDetailsSharedCollection: IPriceRelatedOptionDetails[] = [];

  editForm: PriceRelatedOptionFormGroup = this.priceRelatedOptionFormService.createPriceRelatedOptionFormGroup();

  constructor(
    protected priceRelatedOptionService: PriceRelatedOptionService,
    protected priceRelatedOptionFormService: PriceRelatedOptionFormService,
    protected optionTypeService: OptionTypeService,
    protected priceRelatedOptionDetailsService: PriceRelatedOptionDetailsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOptionType = (o1: IOptionType | null, o2: IOptionType | null): boolean => this.optionTypeService.compareOptionType(o1, o2);

  comparePriceRelatedOptionDetails = (o1: IPriceRelatedOptionDetails | null, o2: IPriceRelatedOptionDetails | null): boolean =>
    this.priceRelatedOptionDetailsService.comparePriceRelatedOptionDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priceRelatedOption }) => {
      this.priceRelatedOption = priceRelatedOption;
      if (priceRelatedOption) {
        this.updateForm(priceRelatedOption);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const priceRelatedOption = this.priceRelatedOptionFormService.getPriceRelatedOption(this.editForm);
    if (priceRelatedOption.id !== null) {
      this.subscribeToSaveResponse(this.priceRelatedOptionService.update(priceRelatedOption));
    } else {
      this.subscribeToSaveResponse(this.priceRelatedOptionService.create(priceRelatedOption));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPriceRelatedOption>>): void {
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

  protected updateForm(priceRelatedOption: IPriceRelatedOption): void {
    this.priceRelatedOption = priceRelatedOption;
    this.priceRelatedOptionFormService.resetForm(this.editForm, priceRelatedOption);

    this.optionTypesSharedCollection = this.optionTypeService.addOptionTypeToCollectionIfMissing<IOptionType>(
      this.optionTypesSharedCollection,
      priceRelatedOption.optionType
    );
    this.priceRelatedOptionDetailsSharedCollection =
      this.priceRelatedOptionDetailsService.addPriceRelatedOptionDetailsToCollectionIfMissing<IPriceRelatedOptionDetails>(
        this.priceRelatedOptionDetailsSharedCollection,
        ...(priceRelatedOption.priceRelatedOptionDetails ?? [])
      );
  }

  protected loadRelationshipsOptions(): void {
    this.optionTypeService
      .query()
      .pipe(map((res: HttpResponse<IOptionType[]>) => res.body ?? []))
      .pipe(
        map((optionTypes: IOptionType[]) =>
          this.optionTypeService.addOptionTypeToCollectionIfMissing<IOptionType>(optionTypes, this.priceRelatedOption?.optionType)
        )
      )
      .subscribe((optionTypes: IOptionType[]) => (this.optionTypesSharedCollection = optionTypes));

    this.priceRelatedOptionDetailsService
      .query()
      .pipe(map((res: HttpResponse<IPriceRelatedOptionDetails[]>) => res.body ?? []))
      .pipe(
        map((priceRelatedOptionDetails: IPriceRelatedOptionDetails[]) =>
          this.priceRelatedOptionDetailsService.addPriceRelatedOptionDetailsToCollectionIfMissing<IPriceRelatedOptionDetails>(
            priceRelatedOptionDetails,
            ...(this.priceRelatedOption?.priceRelatedOptionDetails ?? [])
          )
        )
      )
      .subscribe(
        (priceRelatedOptionDetails: IPriceRelatedOptionDetails[]) =>
          (this.priceRelatedOptionDetailsSharedCollection = priceRelatedOptionDetails)
      );
  }
}
