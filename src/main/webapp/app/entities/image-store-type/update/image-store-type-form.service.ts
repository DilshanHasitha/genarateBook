import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IImageStoreType, NewImageStoreType } from '../image-store-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImageStoreType for edit and NewImageStoreTypeFormGroupInput for create.
 */
type ImageStoreTypeFormGroupInput = IImageStoreType | PartialWithRequiredKeyOf<NewImageStoreType>;

type ImageStoreTypeFormDefaults = Pick<NewImageStoreType, 'id' | 'isActive'>;

type ImageStoreTypeFormGroupContent = {
  id: FormControl<IImageStoreType['id'] | NewImageStoreType['id']>;
  imageStoreTypeCode: FormControl<IImageStoreType['imageStoreTypeCode']>;
  imageStoreTypeDescription: FormControl<IImageStoreType['imageStoreTypeDescription']>;
  isActive: FormControl<IImageStoreType['isActive']>;
};

export type ImageStoreTypeFormGroup = FormGroup<ImageStoreTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImageStoreTypeFormService {
  createImageStoreTypeFormGroup(imageStoreType: ImageStoreTypeFormGroupInput = { id: null }): ImageStoreTypeFormGroup {
    const imageStoreTypeRawValue = {
      ...this.getFormDefaults(),
      ...imageStoreType,
    };
    return new FormGroup<ImageStoreTypeFormGroupContent>({
      id: new FormControl(
        { value: imageStoreTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      imageStoreTypeCode: new FormControl(imageStoreTypeRawValue.imageStoreTypeCode, {
        validators: [Validators.required],
      }),
      imageStoreTypeDescription: new FormControl(imageStoreTypeRawValue.imageStoreTypeDescription, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(imageStoreTypeRawValue.isActive),
    });
  }

  getImageStoreType(form: ImageStoreTypeFormGroup): IImageStoreType | NewImageStoreType {
    return form.getRawValue() as IImageStoreType | NewImageStoreType;
  }

  resetForm(form: ImageStoreTypeFormGroup, imageStoreType: ImageStoreTypeFormGroupInput): void {
    const imageStoreTypeRawValue = { ...this.getFormDefaults(), ...imageStoreType };
    form.reset(
      {
        ...imageStoreTypeRawValue,
        id: { value: imageStoreTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ImageStoreTypeFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
