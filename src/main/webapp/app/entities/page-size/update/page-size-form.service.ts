import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPageSize, NewPageSize } from '../page-size.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPageSize for edit and NewPageSizeFormGroupInput for create.
 */
type PageSizeFormGroupInput = IPageSize | PartialWithRequiredKeyOf<NewPageSize>;

type PageSizeFormDefaults = Pick<NewPageSize, 'id' | 'isActive'>;

type PageSizeFormGroupContent = {
  id: FormControl<IPageSize['id'] | NewPageSize['id']>;
  code: FormControl<IPageSize['code']>;
  description: FormControl<IPageSize['description']>;
  isActive: FormControl<IPageSize['isActive']>;
  width: FormControl<IPageSize['width']>;
  height: FormControl<IPageSize['height']>;
};

export type PageSizeFormGroup = FormGroup<PageSizeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PageSizeFormService {
  createPageSizeFormGroup(pageSize: PageSizeFormGroupInput = { id: null }): PageSizeFormGroup {
    const pageSizeRawValue = {
      ...this.getFormDefaults(),
      ...pageSize,
    };
    return new FormGroup<PageSizeFormGroupContent>({
      id: new FormControl(
        { value: pageSizeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(pageSizeRawValue.code),
      description: new FormControl(pageSizeRawValue.description),
      isActive: new FormControl(pageSizeRawValue.isActive),
      width: new FormControl(pageSizeRawValue.width),
      height: new FormControl(pageSizeRawValue.height),
    });
  }

  getPageSize(form: PageSizeFormGroup): IPageSize | NewPageSize {
    return form.getRawValue() as IPageSize | NewPageSize;
  }

  resetForm(form: PageSizeFormGroup, pageSize: PageSizeFormGroupInput): void {
    const pageSizeRawValue = { ...this.getFormDefaults(), ...pageSize };
    form.reset(
      {
        ...pageSizeRawValue,
        id: { value: pageSizeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PageSizeFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
