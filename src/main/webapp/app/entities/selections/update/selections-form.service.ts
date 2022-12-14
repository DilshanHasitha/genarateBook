import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISelections, NewSelections } from '../selections.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISelections for edit and NewSelectionsFormGroupInput for create.
 */
type SelectionsFormGroupInput = ISelections | PartialWithRequiredKeyOf<NewSelections>;

type SelectionsFormDefaults = Pick<NewSelections, 'id' | 'isActive'>;

type SelectionsFormGroupContent = {
  id: FormControl<ISelections['id'] | NewSelections['id']>;
  avatarCode: FormControl<ISelections['avatarCode']>;
  styleCode: FormControl<ISelections['styleCode']>;
  optionCode: FormControl<ISelections['optionCode']>;
  image: FormControl<ISelections['image']>;
  height: FormControl<ISelections['height']>;
  x: FormControl<ISelections['x']>;
  y: FormControl<ISelections['y']>;
  isActive: FormControl<ISelections['isActive']>;
  width: FormControl<ISelections['width']>;
  avatarAttributesCode: FormControl<ISelections['avatarAttributesCode']>;
  avatarStyle: FormControl<ISelections['avatarStyle']>;
};

export type SelectionsFormGroup = FormGroup<SelectionsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SelectionsFormService {
  createSelectionsFormGroup(selections: SelectionsFormGroupInput = { id: null }): SelectionsFormGroup {
    const selectionsRawValue = {
      ...this.getFormDefaults(),
      ...selections,
    };
    return new FormGroup<SelectionsFormGroupContent>({
      id: new FormControl(
        { value: selectionsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      avatarCode: new FormControl(selectionsRawValue.avatarCode),
      styleCode: new FormControl(selectionsRawValue.styleCode),
      optionCode: new FormControl(selectionsRawValue.optionCode),
      image: new FormControl(selectionsRawValue.image),
      height: new FormControl(selectionsRawValue.height),
      x: new FormControl(selectionsRawValue.x),
      y: new FormControl(selectionsRawValue.y),
      isActive: new FormControl(selectionsRawValue.isActive),
      width: new FormControl(selectionsRawValue.width),
      avatarAttributesCode: new FormControl(selectionsRawValue.avatarAttributesCode),
      avatarStyle: new FormControl(selectionsRawValue.avatarStyle),
    });
  }

  getSelections(form: SelectionsFormGroup): ISelections | NewSelections {
    return form.getRawValue() as ISelections | NewSelections;
  }

  resetForm(form: SelectionsFormGroup, selections: SelectionsFormGroupInput): void {
    const selectionsRawValue = { ...this.getFormDefaults(), ...selections };
    form.reset(
      {
        ...selectionsRawValue,
        id: { value: selectionsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SelectionsFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
