import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPriceRelatedOptionDetails, NewPriceRelatedOptionDetails } from '../price-related-option-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPriceRelatedOptionDetails for edit and NewPriceRelatedOptionDetailsFormGroupInput for create.
 */
type PriceRelatedOptionDetailsFormGroupInput = IPriceRelatedOptionDetails | PartialWithRequiredKeyOf<NewPriceRelatedOptionDetails>;

type PriceRelatedOptionDetailsFormDefaults = Pick<NewPriceRelatedOptionDetails, 'id' | 'priceRelatedOptions'>;

type PriceRelatedOptionDetailsFormGroupContent = {
  id: FormControl<IPriceRelatedOptionDetails['id'] | NewPriceRelatedOptionDetails['id']>;
  description: FormControl<IPriceRelatedOptionDetails['description']>;
  price: FormControl<IPriceRelatedOptionDetails['price']>;
  priceRelatedOptions: FormControl<IPriceRelatedOptionDetails['priceRelatedOptions']>;
};

export type PriceRelatedOptionDetailsFormGroup = FormGroup<PriceRelatedOptionDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PriceRelatedOptionDetailsFormService {
  createPriceRelatedOptionDetailsFormGroup(
    priceRelatedOptionDetails: PriceRelatedOptionDetailsFormGroupInput = { id: null }
  ): PriceRelatedOptionDetailsFormGroup {
    const priceRelatedOptionDetailsRawValue = {
      ...this.getFormDefaults(),
      ...priceRelatedOptionDetails,
    };
    return new FormGroup<PriceRelatedOptionDetailsFormGroupContent>({
      id: new FormControl(
        { value: priceRelatedOptionDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      description: new FormControl(priceRelatedOptionDetailsRawValue.description, {
        validators: [Validators.required],
      }),
      price: new FormControl(priceRelatedOptionDetailsRawValue.price, {
        validators: [Validators.required],
      }),
      priceRelatedOptions: new FormControl(priceRelatedOptionDetailsRawValue.priceRelatedOptions ?? []),
    });
  }

  getPriceRelatedOptionDetails(form: PriceRelatedOptionDetailsFormGroup): IPriceRelatedOptionDetails | NewPriceRelatedOptionDetails {
    return form.getRawValue() as IPriceRelatedOptionDetails | NewPriceRelatedOptionDetails;
  }

  resetForm(form: PriceRelatedOptionDetailsFormGroup, priceRelatedOptionDetails: PriceRelatedOptionDetailsFormGroupInput): void {
    const priceRelatedOptionDetailsRawValue = { ...this.getFormDefaults(), ...priceRelatedOptionDetails };
    form.reset(
      {
        ...priceRelatedOptionDetailsRawValue,
        id: { value: priceRelatedOptionDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PriceRelatedOptionDetailsFormDefaults {
    return {
      id: null,
      priceRelatedOptions: [],
    };
  }
}
