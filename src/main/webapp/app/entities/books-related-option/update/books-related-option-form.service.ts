import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBooksRelatedOption, NewBooksRelatedOption } from '../books-related-option.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBooksRelatedOption for edit and NewBooksRelatedOptionFormGroupInput for create.
 */
type BooksRelatedOptionFormGroupInput = IBooksRelatedOption | PartialWithRequiredKeyOf<NewBooksRelatedOption>;

type BooksRelatedOptionFormDefaults = Pick<NewBooksRelatedOption, 'id' | 'isActive' | 'booksRelatedOptionDetails' | 'books'>;

type BooksRelatedOptionFormGroupContent = {
  id: FormControl<IBooksRelatedOption['id'] | NewBooksRelatedOption['id']>;
  code: FormControl<IBooksRelatedOption['code']>;
  name: FormControl<IBooksRelatedOption['name']>;
  isActive: FormControl<IBooksRelatedOption['isActive']>;
  booksRelatedOptionDetails: FormControl<IBooksRelatedOption['booksRelatedOptionDetails']>;
  books: FormControl<IBooksRelatedOption['books']>;
};

export type BooksRelatedOptionFormGroup = FormGroup<BooksRelatedOptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BooksRelatedOptionFormService {
  createBooksRelatedOptionFormGroup(booksRelatedOption: BooksRelatedOptionFormGroupInput = { id: null }): BooksRelatedOptionFormGroup {
    const booksRelatedOptionRawValue = {
      ...this.getFormDefaults(),
      ...booksRelatedOption,
    };
    return new FormGroup<BooksRelatedOptionFormGroupContent>({
      id: new FormControl(
        { value: booksRelatedOptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(booksRelatedOptionRawValue.code),
      name: new FormControl(booksRelatedOptionRawValue.name, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(booksRelatedOptionRawValue.isActive),
      booksRelatedOptionDetails: new FormControl(booksRelatedOptionRawValue.booksRelatedOptionDetails ?? []),
      books: new FormControl(booksRelatedOptionRawValue.books ?? []),
    });
  }

  getBooksRelatedOption(form: BooksRelatedOptionFormGroup): IBooksRelatedOption | NewBooksRelatedOption {
    return form.getRawValue() as IBooksRelatedOption | NewBooksRelatedOption;
  }

  resetForm(form: BooksRelatedOptionFormGroup, booksRelatedOption: BooksRelatedOptionFormGroupInput): void {
    const booksRelatedOptionRawValue = { ...this.getFormDefaults(), ...booksRelatedOption };
    form.reset(
      {
        ...booksRelatedOptionRawValue,
        id: { value: booksRelatedOptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BooksRelatedOptionFormDefaults {
    return {
      id: null,
      isActive: false,
      booksRelatedOptionDetails: [],
      books: [],
    };
  }
}
