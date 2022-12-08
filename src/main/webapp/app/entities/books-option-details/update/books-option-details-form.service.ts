import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBooksOptionDetails, NewBooksOptionDetails } from '../books-option-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBooksOptionDetails for edit and NewBooksOptionDetailsFormGroupInput for create.
 */
type BooksOptionDetailsFormGroupInput = IBooksOptionDetails | PartialWithRequiredKeyOf<NewBooksOptionDetails>;

type BooksOptionDetailsFormDefaults = Pick<NewBooksOptionDetails, 'id' | 'isActive'>;

type BooksOptionDetailsFormGroupContent = {
  id: FormControl<IBooksOptionDetails['id'] | NewBooksOptionDetails['id']>;
  avatarAttributes: FormControl<IBooksOptionDetails['avatarAttributes']>;
  avatarCharactor: FormControl<IBooksOptionDetails['avatarCharactor']>;
  style: FormControl<IBooksOptionDetails['style']>;
  option: FormControl<IBooksOptionDetails['option']>;
  isActive: FormControl<IBooksOptionDetails['isActive']>;
  books: FormControl<IBooksOptionDetails['books']>;
};

export type BooksOptionDetailsFormGroup = FormGroup<BooksOptionDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BooksOptionDetailsFormService {
  createBooksOptionDetailsFormGroup(booksOptionDetails: BooksOptionDetailsFormGroupInput = { id: null }): BooksOptionDetailsFormGroup {
    const booksOptionDetailsRawValue = {
      ...this.getFormDefaults(),
      ...booksOptionDetails,
    };
    return new FormGroup<BooksOptionDetailsFormGroupContent>({
      id: new FormControl(
        { value: booksOptionDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      avatarAttributes: new FormControl(booksOptionDetailsRawValue.avatarAttributes),
      avatarCharactor: new FormControl(booksOptionDetailsRawValue.avatarCharactor),
      style: new FormControl(booksOptionDetailsRawValue.style),
      option: new FormControl(booksOptionDetailsRawValue.option),
      isActive: new FormControl(booksOptionDetailsRawValue.isActive),
      books: new FormControl(booksOptionDetailsRawValue.books),
    });
  }

  getBooksOptionDetails(form: BooksOptionDetailsFormGroup): IBooksOptionDetails | NewBooksOptionDetails {
    return form.getRawValue() as IBooksOptionDetails | NewBooksOptionDetails;
  }

  resetForm(form: BooksOptionDetailsFormGroup, booksOptionDetails: BooksOptionDetailsFormGroupInput): void {
    const booksOptionDetailsRawValue = { ...this.getFormDefaults(), ...booksOptionDetails };
    form.reset(
      {
        ...booksOptionDetailsRawValue,
        id: { value: booksOptionDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BooksOptionDetailsFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
