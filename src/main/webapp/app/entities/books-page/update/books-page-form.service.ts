import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBooksPage, NewBooksPage } from '../books-page.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBooksPage for edit and NewBooksPageFormGroupInput for create.
 */
type BooksPageFormGroupInput = IBooksPage | PartialWithRequiredKeyOf<NewBooksPage>;

type BooksPageFormDefaults = Pick<NewBooksPage, 'id' | 'isActive' | 'pageDetails' | 'books'>;

type BooksPageFormGroupContent = {
  id: FormControl<IBooksPage['id'] | NewBooksPage['id']>;
  num: FormControl<IBooksPage['num']>;
  isActive: FormControl<IBooksPage['isActive']>;
  pageDetails: FormControl<IBooksPage['pageDetails']>;
  books: FormControl<IBooksPage['books']>;
};

export type BooksPageFormGroup = FormGroup<BooksPageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BooksPageFormService {
  createBooksPageFormGroup(booksPage: BooksPageFormGroupInput = { id: null }): BooksPageFormGroup {
    const booksPageRawValue = {
      ...this.getFormDefaults(),
      ...booksPage,
    };
    return new FormGroup<BooksPageFormGroupContent>({
      id: new FormControl(
        { value: booksPageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      num: new FormControl(booksPageRawValue.num, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(booksPageRawValue.isActive),
      pageDetails: new FormControl(booksPageRawValue.pageDetails ?? []),
      books: new FormControl(booksPageRawValue.books ?? []),
    });
  }

  getBooksPage(form: BooksPageFormGroup): IBooksPage | NewBooksPage {
    return form.getRawValue() as IBooksPage | NewBooksPage;
  }

  resetForm(form: BooksPageFormGroup, booksPage: BooksPageFormGroupInput): void {
    const booksPageRawValue = { ...this.getFormDefaults(), ...booksPage };
    form.reset(
      {
        ...booksPageRawValue,
        id: { value: booksPageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BooksPageFormDefaults {
    return {
      id: null,
      isActive: false,
      pageDetails: [],
      books: [],
    };
  }
}
