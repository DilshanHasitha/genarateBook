import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOptions, NewOptions } from '../options.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOptions for edit and NewOptionsFormGroupInput for create.
 */
type OptionsFormGroupInput = IOptions | PartialWithRequiredKeyOf<NewOptions>;

type OptionsFormDefaults = Pick<NewOptions, 'id' | 'isActive' | 'styles' | 'avatarAttributes'>;

type OptionsFormGroupContent = {
  id: FormControl<IOptions['id'] | NewOptions['id']>;
  code: FormControl<IOptions['code']>;
  description: FormControl<IOptions['description']>;
  imgURL: FormControl<IOptions['imgURL']>;
  isActive: FormControl<IOptions['isActive']>;
  styles: FormControl<IOptions['styles']>;
  avatarAttributes: FormControl<IOptions['avatarAttributes']>;
};

export type OptionsFormGroup = FormGroup<OptionsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OptionsFormService {
  createOptionsFormGroup(options: OptionsFormGroupInput = { id: null }): OptionsFormGroup {
    const optionsRawValue = {
      ...this.getFormDefaults(),
      ...options,
    };
    return new FormGroup<OptionsFormGroupContent>({
      id: new FormControl(
        { value: optionsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(optionsRawValue.code),
      description: new FormControl(optionsRawValue.description, {
        validators: [Validators.required],
      }),
      imgURL: new FormControl(optionsRawValue.imgURL),
      isActive: new FormControl(optionsRawValue.isActive),
      styles: new FormControl(optionsRawValue.styles ?? []),
      avatarAttributes: new FormControl(optionsRawValue.avatarAttributes ?? []),
    });
  }

  getOptions(form: OptionsFormGroup): IOptions | NewOptions {
    return form.getRawValue() as IOptions | NewOptions;
  }

  resetForm(form: OptionsFormGroup, options: OptionsFormGroupInput): void {
    const optionsRawValue = { ...this.getFormDefaults(), ...options };
    form.reset(
      {
        ...optionsRawValue,
        id: { value: optionsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OptionsFormDefaults {
    return {
      id: null,
      isActive: false,
      styles: [],
      avatarAttributes: [],
    };
  }
}
