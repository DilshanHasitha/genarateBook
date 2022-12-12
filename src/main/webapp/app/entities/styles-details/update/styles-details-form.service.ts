import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStylesDetails, NewStylesDetails } from '../styles-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStylesDetails for edit and NewStylesDetailsFormGroupInput for create.
 */
type StylesDetailsFormGroupInput = IStylesDetails | PartialWithRequiredKeyOf<NewStylesDetails>;

type StylesDetailsFormDefaults = Pick<NewStylesDetails, 'id' | 'isActive'>;

type StylesDetailsFormGroupContent = {
  id: FormControl<IStylesDetails['id'] | NewStylesDetails['id']>;
  code: FormControl<IStylesDetails['code']>;
  description: FormControl<IStylesDetails['description']>;
  isActive: FormControl<IStylesDetails['isActive']>;
};

export type StylesDetailsFormGroup = FormGroup<StylesDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StylesDetailsFormService {
  createStylesDetailsFormGroup(stylesDetails: StylesDetailsFormGroupInput = { id: null }): StylesDetailsFormGroup {
    const stylesDetailsRawValue = {
      ...this.getFormDefaults(),
      ...stylesDetails,
    };
    return new FormGroup<StylesDetailsFormGroupContent>({
      id: new FormControl(
        { value: stylesDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(stylesDetailsRawValue.code),
      description: new FormControl(stylesDetailsRawValue.description),
      isActive: new FormControl(stylesDetailsRawValue.isActive),
    });
  }

  getStylesDetails(form: StylesDetailsFormGroup): IStylesDetails | NewStylesDetails {
    return form.getRawValue() as IStylesDetails | NewStylesDetails;
  }

  resetForm(form: StylesDetailsFormGroup, stylesDetails: StylesDetailsFormGroupInput): void {
    const stylesDetailsRawValue = { ...this.getFormDefaults(), ...stylesDetails };
    form.reset(
      {
        ...stylesDetailsRawValue,
        id: { value: stylesDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): StylesDetailsFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
