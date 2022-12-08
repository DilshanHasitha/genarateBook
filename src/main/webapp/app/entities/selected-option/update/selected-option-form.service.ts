import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISelectedOption, NewSelectedOption } from '../selected-option.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISelectedOption for edit and NewSelectedOptionFormGroupInput for create.
 */
type SelectedOptionFormGroupInput = ISelectedOption | PartialWithRequiredKeyOf<NewSelectedOption>;

type SelectedOptionFormDefaults = Pick<NewSelectedOption, 'id' | 'selectedOptionDetails'>;

type SelectedOptionFormGroupContent = {
  id: FormControl<ISelectedOption['id'] | NewSelectedOption['id']>;
  code: FormControl<ISelectedOption['code']>;
  date: FormControl<ISelectedOption['date']>;
  books: FormControl<ISelectedOption['books']>;
  customer: FormControl<ISelectedOption['customer']>;
  selectedOptionDetails: FormControl<ISelectedOption['selectedOptionDetails']>;
};

export type SelectedOptionFormGroup = FormGroup<SelectedOptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SelectedOptionFormService {
  createSelectedOptionFormGroup(selectedOption: SelectedOptionFormGroupInput = { id: null }): SelectedOptionFormGroup {
    const selectedOptionRawValue = {
      ...this.getFormDefaults(),
      ...selectedOption,
    };
    return new FormGroup<SelectedOptionFormGroupContent>({
      id: new FormControl(
        { value: selectedOptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(selectedOptionRawValue.code),
      date: new FormControl(selectedOptionRawValue.date),
      books: new FormControl(selectedOptionRawValue.books),
      customer: new FormControl(selectedOptionRawValue.customer),
      selectedOptionDetails: new FormControl(selectedOptionRawValue.selectedOptionDetails ?? []),
    });
  }

  getSelectedOption(form: SelectedOptionFormGroup): ISelectedOption | NewSelectedOption {
    return form.getRawValue() as ISelectedOption | NewSelectedOption;
  }

  resetForm(form: SelectedOptionFormGroup, selectedOption: SelectedOptionFormGroupInput): void {
    const selectedOptionRawValue = { ...this.getFormDefaults(), ...selectedOption };
    form.reset(
      {
        ...selectedOptionRawValue,
        id: { value: selectedOptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SelectedOptionFormDefaults {
    return {
      id: null,
      selectedOptionDetails: [],
    };
  }
}
