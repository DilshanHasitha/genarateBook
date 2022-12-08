import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPriceRelatedOption, NewPriceRelatedOption } from '../price-related-option.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPriceRelatedOption for edit and NewPriceRelatedOptionFormGroupInput for create.
 */
type PriceRelatedOptionFormGroupInput = IPriceRelatedOption | PartialWithRequiredKeyOf<NewPriceRelatedOption>;

type PriceRelatedOptionFormDefaults = Pick<NewPriceRelatedOption, 'id' | 'isActive' | 'priceRelatedOptionDetails' | 'books'>;

type PriceRelatedOptionFormGroupContent = {
  id: FormControl<IPriceRelatedOption['id'] | NewPriceRelatedOption['id']>;
  code: FormControl<IPriceRelatedOption['code']>;
  name: FormControl<IPriceRelatedOption['name']>;
  isActive: FormControl<IPriceRelatedOption['isActive']>;
  optionType: FormControl<IPriceRelatedOption['optionType']>;
  priceRelatedOptionDetails: FormControl<IPriceRelatedOption['priceRelatedOptionDetails']>;
  books: FormControl<IPriceRelatedOption['books']>;
};

export type PriceRelatedOptionFormGroup = FormGroup<PriceRelatedOptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PriceRelatedOptionFormService {
  createPriceRelatedOptionFormGroup(priceRelatedOption: PriceRelatedOptionFormGroupInput = { id: null }): PriceRelatedOptionFormGroup {
    const priceRelatedOptionRawValue = {
      ...this.getFormDefaults(),
      ...priceRelatedOption,
    };
    return new FormGroup<PriceRelatedOptionFormGroupContent>({
      id: new FormControl(
        { value: priceRelatedOptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(priceRelatedOptionRawValue.code),
      name: new FormControl(priceRelatedOptionRawValue.name, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(priceRelatedOptionRawValue.isActive),
      optionType: new FormControl(priceRelatedOptionRawValue.optionType),
      priceRelatedOptionDetails: new FormControl(priceRelatedOptionRawValue.priceRelatedOptionDetails ?? []),
      books: new FormControl(priceRelatedOptionRawValue.books ?? []),
    });
  }

  getPriceRelatedOption(form: PriceRelatedOptionFormGroup): IPriceRelatedOption | NewPriceRelatedOption {
    return form.getRawValue() as IPriceRelatedOption | NewPriceRelatedOption;
  }

  resetForm(form: PriceRelatedOptionFormGroup, priceRelatedOption: PriceRelatedOptionFormGroupInput): void {
    const priceRelatedOptionRawValue = { ...this.getFormDefaults(), ...priceRelatedOption };
    form.reset(
      {
        ...priceRelatedOptionRawValue,
        id: { value: priceRelatedOptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PriceRelatedOptionFormDefaults {
    return {
      id: null,
      isActive: false,
      priceRelatedOptionDetails: [],
      books: [],
    };
  }
}
