import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrgProperty, NewOrgProperty } from '../org-property.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrgProperty for edit and NewOrgPropertyFormGroupInput for create.
 */
type OrgPropertyFormGroupInput = IOrgProperty | PartialWithRequiredKeyOf<NewOrgProperty>;

type OrgPropertyFormDefaults = Pick<NewOrgProperty, 'id' | 'isActive'>;

type OrgPropertyFormGroupContent = {
  id: FormControl<IOrgProperty['id'] | NewOrgProperty['id']>;
  name: FormControl<IOrgProperty['name']>;
  description: FormControl<IOrgProperty['description']>;
  isActive: FormControl<IOrgProperty['isActive']>;
};

export type OrgPropertyFormGroup = FormGroup<OrgPropertyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrgPropertyFormService {
  createOrgPropertyFormGroup(orgProperty: OrgPropertyFormGroupInput = { id: null }): OrgPropertyFormGroup {
    const orgPropertyRawValue = {
      ...this.getFormDefaults(),
      ...orgProperty,
    };
    return new FormGroup<OrgPropertyFormGroupContent>({
      id: new FormControl(
        { value: orgPropertyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(orgPropertyRawValue.name),
      description: new FormControl(orgPropertyRawValue.description),
      isActive: new FormControl(orgPropertyRawValue.isActive),
    });
  }

  getOrgProperty(form: OrgPropertyFormGroup): IOrgProperty | NewOrgProperty {
    return form.getRawValue() as IOrgProperty | NewOrgProperty;
  }

  resetForm(form: OrgPropertyFormGroup, orgProperty: OrgPropertyFormGroupInput): void {
    const orgPropertyRawValue = { ...this.getFormDefaults(), ...orgProperty };
    form.reset(
      {
        ...orgPropertyRawValue,
        id: { value: orgPropertyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrgPropertyFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
