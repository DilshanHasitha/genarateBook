import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISelectedOptionDetails, NewSelectedOptionDetails } from '../selected-option-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISelectedOptionDetails for edit and NewSelectedOptionDetailsFormGroupInput for create.
 */
type SelectedOptionDetailsFormGroupInput = ISelectedOptionDetails | PartialWithRequiredKeyOf<NewSelectedOptionDetails>;

type SelectedOptionDetailsFormDefaults = Pick<NewSelectedOptionDetails, 'id' | 'isActive' | 'selectedOptions'>;

type SelectedOptionDetailsFormGroupContent = {
  id: FormControl<ISelectedOptionDetails['id'] | NewSelectedOptionDetails['id']>;
  code: FormControl<ISelectedOptionDetails['code']>;
  name: FormControl<ISelectedOptionDetails['name']>;
  selectedValue: FormControl<ISelectedOptionDetails['selectedValue']>;
  isActive: FormControl<ISelectedOptionDetails['isActive']>;
  selectedOptions: FormControl<ISelectedOptionDetails['selectedOptions']>;
};

export type SelectedOptionDetailsFormGroup = FormGroup<SelectedOptionDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SelectedOptionDetailsFormService {
  createSelectedOptionDetailsFormGroup(
    selectedOptionDetails: SelectedOptionDetailsFormGroupInput = { id: null }
  ): SelectedOptionDetailsFormGroup {
    const selectedOptionDetailsRawValue = {
      ...this.getFormDefaults(),
      ...selectedOptionDetails,
    };
    return new FormGroup<SelectedOptionDetailsFormGroupContent>({
      id: new FormControl(
        { value: selectedOptionDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(selectedOptionDetailsRawValue.code),
      name: new FormControl(selectedOptionDetailsRawValue.name),
      selectedValue: new FormControl(selectedOptionDetailsRawValue.selectedValue),
      isActive: new FormControl(selectedOptionDetailsRawValue.isActive),
      selectedOptions: new FormControl(selectedOptionDetailsRawValue.selectedOptions ?? []),
    });
  }

  getSelectedOptionDetails(form: SelectedOptionDetailsFormGroup): ISelectedOptionDetails | NewSelectedOptionDetails {
    return form.getRawValue() as ISelectedOptionDetails | NewSelectedOptionDetails;
  }

  resetForm(form: SelectedOptionDetailsFormGroup, selectedOptionDetails: SelectedOptionDetailsFormGroupInput): void {
    const selectedOptionDetailsRawValue = { ...this.getFormDefaults(), ...selectedOptionDetails };
    form.reset(
      {
        ...selectedOptionDetailsRawValue,
        id: { value: selectedOptionDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SelectedOptionDetailsFormDefaults {
    return {
      id: null,
      isActive: false,
      selectedOptions: [],
    };
  }
}
