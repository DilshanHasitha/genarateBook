import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAvatarAttributes, NewAvatarAttributes } from '../avatar-attributes.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAvatarAttributes for edit and NewAvatarAttributesFormGroupInput for create.
 */
type AvatarAttributesFormGroupInput = IAvatarAttributes | PartialWithRequiredKeyOf<NewAvatarAttributes>;

type AvatarAttributesFormDefaults = Pick<NewAvatarAttributes, 'id' | 'isActive' | 'avatarCharactors' | 'books' | 'styles'>;

type AvatarAttributesFormGroupContent = {
  id: FormControl<IAvatarAttributes['id'] | NewAvatarAttributes['id']>;
  code: FormControl<IAvatarAttributes['code']>;
  description: FormControl<IAvatarAttributes['description']>;
  isActive: FormControl<IAvatarAttributes['isActive']>;
  avatarCharactors: FormControl<IAvatarAttributes['avatarCharactors']>;
  books: FormControl<IAvatarAttributes['books']>;
  styles: FormControl<IAvatarAttributes['styles']>;
};

export type AvatarAttributesFormGroup = FormGroup<AvatarAttributesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AvatarAttributesFormService {
  createAvatarAttributesFormGroup(avatarAttributes: AvatarAttributesFormGroupInput = { id: null }): AvatarAttributesFormGroup {
    const avatarAttributesRawValue = {
      ...this.getFormDefaults(),
      ...avatarAttributes,
    };
    return new FormGroup<AvatarAttributesFormGroupContent>({
      id: new FormControl(
        { value: avatarAttributesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(avatarAttributesRawValue.code),
      description: new FormControl(avatarAttributesRawValue.description, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(avatarAttributesRawValue.isActive),
      avatarCharactors: new FormControl(avatarAttributesRawValue.avatarCharactors ?? []),
      books: new FormControl(avatarAttributesRawValue.books ?? []),
      styles: new FormControl(avatarAttributesRawValue.styles ?? []),
    });
  }

  getAvatarAttributes(form: AvatarAttributesFormGroup): IAvatarAttributes | NewAvatarAttributes {
    return form.getRawValue() as IAvatarAttributes | NewAvatarAttributes;
  }

  resetForm(form: AvatarAttributesFormGroup, avatarAttributes: AvatarAttributesFormGroupInput): void {
    const avatarAttributesRawValue = { ...this.getFormDefaults(), ...avatarAttributes };
    form.reset(
      {
        ...avatarAttributesRawValue,
        id: { value: avatarAttributesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AvatarAttributesFormDefaults {
    return {
      id: null,
      isActive: false,
      avatarCharactors: [],
      books: [],
      styles: [],
    };
  }
}
