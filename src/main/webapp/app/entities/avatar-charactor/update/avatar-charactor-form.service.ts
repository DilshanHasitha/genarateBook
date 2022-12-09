import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAvatarCharactor, NewAvatarCharactor } from '../avatar-charactor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAvatarCharactor for edit and NewAvatarCharactorFormGroupInput for create.
 */
type AvatarCharactorFormGroupInput = IAvatarCharactor | PartialWithRequiredKeyOf<NewAvatarCharactor>;

type AvatarCharactorFormDefaults = Pick<NewAvatarCharactor, 'id' | 'isActive' | 'avatarAttributes'>;

type AvatarCharactorFormGroupContent = {
  id: FormControl<IAvatarCharactor['id'] | NewAvatarCharactor['id']>;
  code: FormControl<IAvatarCharactor['code']>;
  description: FormControl<IAvatarCharactor['description']>;
  isActive: FormControl<IAvatarCharactor['isActive']>;
  imgUrl: FormControl<IAvatarCharactor['imgUrl']>;
  width: FormControl<IAvatarCharactor['width']>;
  height: FormControl<IAvatarCharactor['height']>;
  x: FormControl<IAvatarCharactor['x']>;
  y: FormControl<IAvatarCharactor['y']>;
  avatarAttributes: FormControl<IAvatarCharactor['avatarAttributes']>;
  layerGroup: FormControl<IAvatarCharactor['layerGroup']>;
};

export type AvatarCharactorFormGroup = FormGroup<AvatarCharactorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AvatarCharactorFormService {
  createAvatarCharactorFormGroup(avatarCharactor: AvatarCharactorFormGroupInput = { id: null }): AvatarCharactorFormGroup {
    const avatarCharactorRawValue = {
      ...this.getFormDefaults(),
      ...avatarCharactor,
    };
    return new FormGroup<AvatarCharactorFormGroupContent>({
      id: new FormControl(
        { value: avatarCharactorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(avatarCharactorRawValue.code),
      description: new FormControl(avatarCharactorRawValue.description, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(avatarCharactorRawValue.isActive),
      imgUrl: new FormControl(avatarCharactorRawValue.imgUrl),
      width: new FormControl(avatarCharactorRawValue.width),
      height: new FormControl(avatarCharactorRawValue.height),
      x: new FormControl(avatarCharactorRawValue.x),
      y: new FormControl(avatarCharactorRawValue.y),
      avatarAttributes: new FormControl(avatarCharactorRawValue.avatarAttributes ?? []),
      layerGroup: new FormControl(avatarCharactorRawValue.layerGroup),
    });
  }

  getAvatarCharactor(form: AvatarCharactorFormGroup): IAvatarCharactor | NewAvatarCharactor {
    return form.getRawValue() as IAvatarCharactor | NewAvatarCharactor;
  }

  resetForm(form: AvatarCharactorFormGroup, avatarCharactor: AvatarCharactorFormGroupInput): void {
    const avatarCharactorRawValue = { ...this.getFormDefaults(), ...avatarCharactor };
    form.reset(
      {
        ...avatarCharactorRawValue,
        id: { value: avatarCharactorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AvatarCharactorFormDefaults {
    return {
      id: null,
      isActive: false,
      avatarAttributes: [],
    };
  }
}
