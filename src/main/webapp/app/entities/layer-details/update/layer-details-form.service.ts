import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILayerDetails, NewLayerDetails } from '../layer-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILayerDetails for edit and NewLayerDetailsFormGroupInput for create.
 */
type LayerDetailsFormGroupInput = ILayerDetails | PartialWithRequiredKeyOf<NewLayerDetails>;

type LayerDetailsFormDefaults = Pick<NewLayerDetails, 'id' | 'layers'>;

type LayerDetailsFormGroupContent = {
  id: FormControl<ILayerDetails['id'] | NewLayerDetails['id']>;
  name: FormControl<ILayerDetails['name']>;
  description: FormControl<ILayerDetails['description']>;
  layers: FormControl<ILayerDetails['layers']>;
};

export type LayerDetailsFormGroup = FormGroup<LayerDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LayerDetailsFormService {
  createLayerDetailsFormGroup(layerDetails: LayerDetailsFormGroupInput = { id: null }): LayerDetailsFormGroup {
    const layerDetailsRawValue = {
      ...this.getFormDefaults(),
      ...layerDetails,
    };
    return new FormGroup<LayerDetailsFormGroupContent>({
      id: new FormControl(
        { value: layerDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(layerDetailsRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(layerDetailsRawValue.description),
      layers: new FormControl(layerDetailsRawValue.layers ?? []),
    });
  }

  getLayerDetails(form: LayerDetailsFormGroup): ILayerDetails | NewLayerDetails {
    return form.getRawValue() as ILayerDetails | NewLayerDetails;
  }

  resetForm(form: LayerDetailsFormGroup, layerDetails: LayerDetailsFormGroupInput): void {
    const layerDetailsRawValue = { ...this.getFormDefaults(), ...layerDetails };
    form.reset(
      {
        ...layerDetailsRawValue,
        id: { value: layerDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LayerDetailsFormDefaults {
    return {
      id: null,
      layers: [],
    };
  }
}
