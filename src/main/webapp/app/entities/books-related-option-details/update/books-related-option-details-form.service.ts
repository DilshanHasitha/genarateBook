import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBooksRelatedOptionDetails, NewBooksRelatedOptionDetails } from '../books-related-option-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBooksRelatedOptionDetails for edit and NewBooksRelatedOptionDetailsFormGroupInput for create.
 */
type BooksRelatedOptionDetailsFormGroupInput = IBooksRelatedOptionDetails | PartialWithRequiredKeyOf<NewBooksRelatedOptionDetails>;

type BooksRelatedOptionDetailsFormDefaults = Pick<NewBooksRelatedOptionDetails, 'id' | 'isActive' | 'booksRelatedOptions'>;

type BooksRelatedOptionDetailsFormGroupContent = {
  id: FormControl<IBooksRelatedOptionDetails['id'] | NewBooksRelatedOptionDetails['id']>;
  code: FormControl<IBooksRelatedOptionDetails['code']>;
  description: FormControl<IBooksRelatedOptionDetails['description']>;
  isActive: FormControl<IBooksRelatedOptionDetails['isActive']>;
  booksRelatedOptions: FormControl<IBooksRelatedOptionDetails['booksRelatedOptions']>;
};

export type BooksRelatedOptionDetailsFormGroup = FormGroup<BooksRelatedOptionDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BooksRelatedOptionDetailsFormService {
  createBooksRelatedOptionDetailsFormGroup(
    booksRelatedOptionDetails: BooksRelatedOptionDetailsFormGroupInput = { id: null }
  ): BooksRelatedOptionDetailsFormGroup {
    const booksRelatedOptionDetailsRawValue = {
      ...this.getFormDefaults(),
      ...booksRelatedOptionDetails,
    };
    return new FormGroup<BooksRelatedOptionDetailsFormGroupContent>({
      id: new FormControl(
        { value: booksRelatedOptionDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(booksRelatedOptionDetailsRawValue.code),
      description: new FormControl(booksRelatedOptionDetailsRawValue.description, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(booksRelatedOptionDetailsRawValue.isActive),
      booksRelatedOptions: new FormControl(booksRelatedOptionDetailsRawValue.booksRelatedOptions ?? []),
    });
  }

  getBooksRelatedOptionDetails(form: BooksRelatedOptionDetailsFormGroup): IBooksRelatedOptionDetails | NewBooksRelatedOptionDetails {
    return form.getRawValue() as IBooksRelatedOptionDetails | NewBooksRelatedOptionDetails;
  }

  resetForm(form: BooksRelatedOptionDetailsFormGroup, booksRelatedOptionDetails: BooksRelatedOptionDetailsFormGroupInput): void {
    const booksRelatedOptionDetailsRawValue = { ...this.getFormDefaults(), ...booksRelatedOptionDetails };
    form.reset(
      {
        ...booksRelatedOptionDetailsRawValue,
        id: { value: booksRelatedOptionDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BooksRelatedOptionDetailsFormDefaults {
    return {
      id: null,
      isActive: false,
      booksRelatedOptions: [],
    };
  }
}
