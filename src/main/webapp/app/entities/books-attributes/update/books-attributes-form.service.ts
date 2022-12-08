import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBooksAttributes, NewBooksAttributes } from '../books-attributes.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBooksAttributes for edit and NewBooksAttributesFormGroupInput for create.
 */
type BooksAttributesFormGroupInput = IBooksAttributes | PartialWithRequiredKeyOf<NewBooksAttributes>;

type BooksAttributesFormDefaults = Pick<NewBooksAttributes, 'id' | 'isActive' | 'books'>;

type BooksAttributesFormGroupContent = {
  id: FormControl<IBooksAttributes['id'] | NewBooksAttributes['id']>;
  code: FormControl<IBooksAttributes['code']>;
  description: FormControl<IBooksAttributes['description']>;
  isActive: FormControl<IBooksAttributes['isActive']>;
  books: FormControl<IBooksAttributes['books']>;
};

export type BooksAttributesFormGroup = FormGroup<BooksAttributesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BooksAttributesFormService {
  createBooksAttributesFormGroup(booksAttributes: BooksAttributesFormGroupInput = { id: null }): BooksAttributesFormGroup {
    const booksAttributesRawValue = {
      ...this.getFormDefaults(),
      ...booksAttributes,
    };
    return new FormGroup<BooksAttributesFormGroupContent>({
      id: new FormControl(
        { value: booksAttributesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(booksAttributesRawValue.code),
      description: new FormControl(booksAttributesRawValue.description),
      isActive: new FormControl(booksAttributesRawValue.isActive),
      books: new FormControl(booksAttributesRawValue.books ?? []),
    });
  }

  getBooksAttributes(form: BooksAttributesFormGroup): IBooksAttributes | NewBooksAttributes {
    return form.getRawValue() as IBooksAttributes | NewBooksAttributes;
  }

  resetForm(form: BooksAttributesFormGroup, booksAttributes: BooksAttributesFormGroupInput): void {
    const booksAttributesRawValue = { ...this.getFormDefaults(), ...booksAttributes };
    form.reset(
      {
        ...booksAttributesRawValue,
        id: { value: booksAttributesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BooksAttributesFormDefaults {
    return {
      id: null,
      isActive: false,
      books: [],
    };
  }
}
