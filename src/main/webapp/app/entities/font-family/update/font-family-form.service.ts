import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFontFamily, NewFontFamily } from '../font-family.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFontFamily for edit and NewFontFamilyFormGroupInput for create.
 */
type FontFamilyFormGroupInput = IFontFamily | PartialWithRequiredKeyOf<NewFontFamily>;

type FontFamilyFormDefaults = Pick<NewFontFamily, 'id' | 'isActive'>;

type FontFamilyFormGroupContent = {
  id: FormControl<IFontFamily['id'] | NewFontFamily['id']>;
  name: FormControl<IFontFamily['name']>;
  url: FormControl<IFontFamily['url']>;
  isActive: FormControl<IFontFamily['isActive']>;
};

export type FontFamilyFormGroup = FormGroup<FontFamilyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FontFamilyFormService {
  createFontFamilyFormGroup(fontFamily: FontFamilyFormGroupInput = { id: null }): FontFamilyFormGroup {
    const fontFamilyRawValue = {
      ...this.getFormDefaults(),
      ...fontFamily,
    };
    return new FormGroup<FontFamilyFormGroupContent>({
      id: new FormControl(
        { value: fontFamilyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(fontFamilyRawValue.name),
      url: new FormControl(fontFamilyRawValue.url),
      isActive: new FormControl(fontFamilyRawValue.isActive),
    });
  }

  getFontFamily(form: FontFamilyFormGroup): IFontFamily | NewFontFamily {
    return form.getRawValue() as IFontFamily | NewFontFamily;
  }

  resetForm(form: FontFamilyFormGroup, fontFamily: FontFamilyFormGroupInput): void {
    const fontFamilyRawValue = { ...this.getFormDefaults(), ...fontFamily };
    form.reset(
      {
        ...fontFamilyRawValue,
        id: { value: fontFamilyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FontFamilyFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
