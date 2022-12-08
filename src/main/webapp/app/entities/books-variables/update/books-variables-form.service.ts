import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBooksVariables, NewBooksVariables } from '../books-variables.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBooksVariables for edit and NewBooksVariablesFormGroupInput for create.
 */
type BooksVariablesFormGroupInput = IBooksVariables | PartialWithRequiredKeyOf<NewBooksVariables>;

type BooksVariablesFormDefaults = Pick<NewBooksVariables, 'id' | 'isActive' | 'books'>;

type BooksVariablesFormGroupContent = {
  id: FormControl<IBooksVariables['id'] | NewBooksVariables['id']>;
  code: FormControl<IBooksVariables['code']>;
  description: FormControl<IBooksVariables['description']>;
  isActive: FormControl<IBooksVariables['isActive']>;
  books: FormControl<IBooksVariables['books']>;
};

export type BooksVariablesFormGroup = FormGroup<BooksVariablesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BooksVariablesFormService {
  createBooksVariablesFormGroup(booksVariables: BooksVariablesFormGroupInput = { id: null }): BooksVariablesFormGroup {
    const booksVariablesRawValue = {
      ...this.getFormDefaults(),
      ...booksVariables,
    };
    return new FormGroup<BooksVariablesFormGroupContent>({
      id: new FormControl(
        { value: booksVariablesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(booksVariablesRawValue.code),
      description: new FormControl(booksVariablesRawValue.description),
      isActive: new FormControl(booksVariablesRawValue.isActive),
      books: new FormControl(booksVariablesRawValue.books ?? []),
    });
  }

  getBooksVariables(form: BooksVariablesFormGroup): IBooksVariables | NewBooksVariables {
    return form.getRawValue() as IBooksVariables | NewBooksVariables;
  }

  resetForm(form: BooksVariablesFormGroup, booksVariables: BooksVariablesFormGroupInput): void {
    const booksVariablesRawValue = { ...this.getFormDefaults(), ...booksVariables };
    form.reset(
      {
        ...booksVariablesRawValue,
        id: { value: booksVariablesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BooksVariablesFormDefaults {
    return {
      id: null,
      isActive: false,
      books: [],
    };
  }
}
