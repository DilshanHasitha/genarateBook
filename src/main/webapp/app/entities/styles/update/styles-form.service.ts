import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStyles, NewStyles } from '../styles.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStyles for edit and NewStylesFormGroupInput for create.
 */
type StylesFormGroupInput = IStyles | PartialWithRequiredKeyOf<NewStyles>;

type StylesFormDefaults = Pick<NewStyles, 'id' | 'isActive'>;

type StylesFormGroupContent = {
  id: FormControl<IStyles['id'] | NewStyles['id']>;
  code: FormControl<IStyles['code']>;
  description: FormControl<IStyles['description']>;
  imgURL: FormControl<IStyles['imgURL']>;
  isActive: FormControl<IStyles['isActive']>;
  width: FormControl<IStyles['width']>;
  height: FormControl<IStyles['height']>;
  x: FormControl<IStyles['x']>;
  y: FormControl<IStyles['y']>;
};

export type StylesFormGroup = FormGroup<StylesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StylesFormService {
  createStylesFormGroup(styles: StylesFormGroupInput = { id: null }): StylesFormGroup {
    const stylesRawValue = {
      ...this.getFormDefaults(),
      ...styles,
    };
    return new FormGroup<StylesFormGroupContent>({
      id: new FormControl(
        { value: stylesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(stylesRawValue.code),
      description: new FormControl(stylesRawValue.description, {
        validators: [Validators.required],
      }),
      imgURL: new FormControl(stylesRawValue.imgURL),
      isActive: new FormControl(stylesRawValue.isActive),
      width: new FormControl(stylesRawValue.width),
      height: new FormControl(stylesRawValue.height),
      x: new FormControl(stylesRawValue.x),
      y: new FormControl(stylesRawValue.y),
    });
  }

  getStyles(form: StylesFormGroup): IStyles | NewStyles {
    return form.getRawValue() as IStyles | NewStyles;
  }

  resetForm(form: StylesFormGroup, styles: StylesFormGroupInput): void {
    const stylesRawValue = { ...this.getFormDefaults(), ...styles };
    form.reset(
      {
        ...stylesRawValue,
        id: { value: stylesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): StylesFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
