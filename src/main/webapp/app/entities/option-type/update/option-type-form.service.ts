import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOptionType, NewOptionType } from '../option-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOptionType for edit and NewOptionTypeFormGroupInput for create.
 */
type OptionTypeFormGroupInput = IOptionType | PartialWithRequiredKeyOf<NewOptionType>;

type OptionTypeFormDefaults = Pick<NewOptionType, 'id' | 'isActive'>;

type OptionTypeFormGroupContent = {
  id: FormControl<IOptionType['id'] | NewOptionType['id']>;
  code: FormControl<IOptionType['code']>;
  description: FormControl<IOptionType['description']>;
  isActive: FormControl<IOptionType['isActive']>;
};

export type OptionTypeFormGroup = FormGroup<OptionTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OptionTypeFormService {
  createOptionTypeFormGroup(optionType: OptionTypeFormGroupInput = { id: null }): OptionTypeFormGroup {
    const optionTypeRawValue = {
      ...this.getFormDefaults(),
      ...optionType,
    };
    return new FormGroup<OptionTypeFormGroupContent>({
      id: new FormControl(
        { value: optionTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(optionTypeRawValue.code),
      description: new FormControl(optionTypeRawValue.description, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(optionTypeRawValue.isActive),
    });
  }

  getOptionType(form: OptionTypeFormGroup): IOptionType | NewOptionType {
    return form.getRawValue() as IOptionType | NewOptionType;
  }

  resetForm(form: OptionTypeFormGroup, optionType: OptionTypeFormGroupInput): void {
    const optionTypeRawValue = { ...this.getFormDefaults(), ...optionType };
    form.reset(
      {
        ...optionTypeRawValue,
        id: { value: optionTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OptionTypeFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
