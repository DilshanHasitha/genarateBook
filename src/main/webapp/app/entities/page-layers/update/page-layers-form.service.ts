import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPageLayers, NewPageLayers } from '../page-layers.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPageLayers for edit and NewPageLayersFormGroupInput for create.
 */
type PageLayersFormGroupInput = IPageLayers | PartialWithRequiredKeyOf<NewPageLayers>;

type PageLayersFormDefaults = Pick<NewPageLayers, 'id' | 'isActive' | 'isEditable' | 'isText' | 'pageElementDetails' | 'booksPages'>;

type PageLayersFormGroupContent = {
  id: FormControl<IPageLayers['id'] | NewPageLayers['id']>;
  layerNo: FormControl<IPageLayers['layerNo']>;
  isActive: FormControl<IPageLayers['isActive']>;
  isEditable: FormControl<IPageLayers['isEditable']>;
  isText: FormControl<IPageLayers['isText']>;
  pageElementDetails: FormControl<IPageLayers['pageElementDetails']>;
  booksPages: FormControl<IPageLayers['booksPages']>;
};

export type PageLayersFormGroup = FormGroup<PageLayersFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PageLayersFormService {
  createPageLayersFormGroup(pageLayers: PageLayersFormGroupInput = { id: null }): PageLayersFormGroup {
    const pageLayersRawValue = {
      ...this.getFormDefaults(),
      ...pageLayers,
    };
    return new FormGroup<PageLayersFormGroupContent>({
      id: new FormControl(
        { value: pageLayersRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      layerNo: new FormControl(pageLayersRawValue.layerNo, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(pageLayersRawValue.isActive),
      isEditable: new FormControl(pageLayersRawValue.isEditable),
      isText: new FormControl(pageLayersRawValue.isText),
      pageElementDetails: new FormControl(pageLayersRawValue.pageElementDetails ?? []),
      booksPages: new FormControl(pageLayersRawValue.booksPages ?? []),
    });
  }

  getPageLayers(form: PageLayersFormGroup): IPageLayers | NewPageLayers {
    return form.getRawValue() as IPageLayers | NewPageLayers;
  }

  resetForm(form: PageLayersFormGroup, pageLayers: PageLayersFormGroupInput): void {
    const pageLayersRawValue = { ...this.getFormDefaults(), ...pageLayers };
    form.reset(
      {
        ...pageLayersRawValue,
        id: { value: pageLayersRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PageLayersFormDefaults {
    return {
      id: null,
      isActive: false,
      isEditable: false,
      isText: false,
      pageElementDetails: [],
      booksPages: [],
    };
  }
}
