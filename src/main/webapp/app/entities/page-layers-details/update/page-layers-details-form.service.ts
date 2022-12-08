import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPageLayersDetails, NewPageLayersDetails } from '../page-layers-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPageLayersDetails for edit and NewPageLayersDetailsFormGroupInput for create.
 */
type PageLayersDetailsFormGroupInput = IPageLayersDetails | PartialWithRequiredKeyOf<NewPageLayersDetails>;

type PageLayersDetailsFormDefaults = Pick<NewPageLayersDetails, 'id' | 'isActive' | 'pageElements'>;

type PageLayersDetailsFormGroupContent = {
  id: FormControl<IPageLayersDetails['id'] | NewPageLayersDetails['id']>;
  name: FormControl<IPageLayersDetails['name']>;
  description: FormControl<IPageLayersDetails['description']>;
  isActive: FormControl<IPageLayersDetails['isActive']>;
  pageElements: FormControl<IPageLayersDetails['pageElements']>;
};

export type PageLayersDetailsFormGroup = FormGroup<PageLayersDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PageLayersDetailsFormService {
  createPageLayersDetailsFormGroup(pageLayersDetails: PageLayersDetailsFormGroupInput = { id: null }): PageLayersDetailsFormGroup {
    const pageLayersDetailsRawValue = {
      ...this.getFormDefaults(),
      ...pageLayersDetails,
    };
    return new FormGroup<PageLayersDetailsFormGroupContent>({
      id: new FormControl(
        { value: pageLayersDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(pageLayersDetailsRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(pageLayersDetailsRawValue.description),
      isActive: new FormControl(pageLayersDetailsRawValue.isActive),
      pageElements: new FormControl(pageLayersDetailsRawValue.pageElements ?? []),
    });
  }

  getPageLayersDetails(form: PageLayersDetailsFormGroup): IPageLayersDetails | NewPageLayersDetails {
    return form.getRawValue() as IPageLayersDetails | NewPageLayersDetails;
  }

  resetForm(form: PageLayersDetailsFormGroup, pageLayersDetails: PageLayersDetailsFormGroupInput): void {
    const pageLayersDetailsRawValue = { ...this.getFormDefaults(), ...pageLayersDetails };
    form.reset(
      {
        ...pageLayersDetailsRawValue,
        id: { value: pageLayersDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PageLayersDetailsFormDefaults {
    return {
      id: null,
      isActive: false,
      pageElements: [],
    };
  }
}
