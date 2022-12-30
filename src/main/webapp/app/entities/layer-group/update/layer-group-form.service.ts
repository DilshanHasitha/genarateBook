import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILayerGroup, NewLayerGroup } from '../layer-group.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILayerGroup for edit and NewLayerGroupFormGroupInput for create.
 */
type LayerGroupFormGroupInput = ILayerGroup | PartialWithRequiredKeyOf<NewLayerGroup>;

type LayerGroupFormDefaults = Pick<NewLayerGroup, 'id' | 'isActive' | 'layers' | 'books'>;

type LayerGroupFormGroupContent = {
  id: FormControl<ILayerGroup['id'] | NewLayerGroup['id']>;
  code: FormControl<ILayerGroup['code']>;
  description: FormControl<ILayerGroup['description']>;
  isActive: FormControl<ILayerGroup['isActive']>;
  imageUrl: FormControl<ILayerGroup['imageUrl']>;
  layers: FormControl<ILayerGroup['layers']>;
  books: FormControl<ILayerGroup['books']>;
};

export type LayerGroupFormGroup = FormGroup<LayerGroupFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LayerGroupFormService {
  createLayerGroupFormGroup(layerGroup: LayerGroupFormGroupInput = { id: null }): LayerGroupFormGroup {
    const layerGroupRawValue = {
      ...this.getFormDefaults(),
      ...layerGroup,
    };
    return new FormGroup<LayerGroupFormGroupContent>({
      id: new FormControl(
        { value: layerGroupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(layerGroupRawValue.code),
      description: new FormControl(layerGroupRawValue.description, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(layerGroupRawValue.isActive),
      imageUrl: new FormControl(layerGroupRawValue.imageUrl),
      layers: new FormControl(layerGroupRawValue.layers ?? []),
      books: new FormControl(layerGroupRawValue.books ?? []),
    });
  }

  getLayerGroup(form: LayerGroupFormGroup): ILayerGroup | NewLayerGroup {
    return form.getRawValue() as ILayerGroup | NewLayerGroup;
  }

  resetForm(form: LayerGroupFormGroup, layerGroup: LayerGroupFormGroupInput): void {
    const layerGroupRawValue = { ...this.getFormDefaults(), ...layerGroup };
    form.reset(
      {
        ...layerGroupRawValue,
        id: { value: layerGroupRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LayerGroupFormDefaults {
    return {
      id: null,
      isActive: false,
      layers: [],
      books: [],
    };
  }
}
