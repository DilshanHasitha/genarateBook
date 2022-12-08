import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILayers, NewLayers } from '../layers.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILayers for edit and NewLayersFormGroupInput for create.
 */
type LayersFormGroupInput = ILayers | PartialWithRequiredKeyOf<NewLayers>;

type LayersFormDefaults = Pick<NewLayers, 'id' | 'isActive' | 'layerdetails' | 'layerGroups'>;

type LayersFormGroupContent = {
  id: FormControl<ILayers['id'] | NewLayers['id']>;
  layerNo: FormControl<ILayers['layerNo']>;
  isActive: FormControl<ILayers['isActive']>;
  layerdetails: FormControl<ILayers['layerdetails']>;
  layerGroups: FormControl<ILayers['layerGroups']>;
};

export type LayersFormGroup = FormGroup<LayersFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LayersFormService {
  createLayersFormGroup(layers: LayersFormGroupInput = { id: null }): LayersFormGroup {
    const layersRawValue = {
      ...this.getFormDefaults(),
      ...layers,
    };
    return new FormGroup<LayersFormGroupContent>({
      id: new FormControl(
        { value: layersRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      layerNo: new FormControl(layersRawValue.layerNo, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(layersRawValue.isActive),
      layerdetails: new FormControl(layersRawValue.layerdetails ?? []),
      layerGroups: new FormControl(layersRawValue.layerGroups ?? []),
    });
  }

  getLayers(form: LayersFormGroup): ILayers | NewLayers {
    return form.getRawValue() as ILayers | NewLayers;
  }

  resetForm(form: LayersFormGroup, layers: LayersFormGroupInput): void {
    const layersRawValue = { ...this.getFormDefaults(), ...layers };
    form.reset(
      {
        ...layersRawValue,
        id: { value: layersRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LayersFormDefaults {
    return {
      id: null,
      isActive: false,
      layerdetails: [],
      layerGroups: [],
    };
  }
}
