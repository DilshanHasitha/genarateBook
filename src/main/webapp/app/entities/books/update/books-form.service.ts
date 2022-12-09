import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBooks, NewBooks } from '../books.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBooks for edit and NewBooksFormGroupInput for create.
 */
type BooksFormGroupInput = IBooks | PartialWithRequiredKeyOf<NewBooks>;

type BooksFormDefaults = Pick<
  NewBooks,
  | 'id'
  | 'isActive'
  | 'booksPages'
  | 'priceRelatedOptions'
  | 'booksRelatedOptions'
  | 'booksAttributes'
  | 'booksVariables'
  | 'avatarAttributes'
  | 'layerGroups'
  | 'selections'
>;

type BooksFormGroupContent = {
  id: FormControl<IBooks['id'] | NewBooks['id']>;
  code: FormControl<IBooks['code']>;
  name: FormControl<IBooks['name']>;
  title: FormControl<IBooks['title']>;
  subTitle: FormControl<IBooks['subTitle']>;
  author: FormControl<IBooks['author']>;
  isActive: FormControl<IBooks['isActive']>;
  noOfPages: FormControl<IBooks['noOfPages']>;
  storeImg: FormControl<IBooks['storeImg']>;
  pageSize: FormControl<IBooks['pageSize']>;
  user: FormControl<IBooks['user']>;
  booksPages: FormControl<IBooks['booksPages']>;
  priceRelatedOptions: FormControl<IBooks['priceRelatedOptions']>;
  booksRelatedOptions: FormControl<IBooks['booksRelatedOptions']>;
  booksAttributes: FormControl<IBooks['booksAttributes']>;
  booksVariables: FormControl<IBooks['booksVariables']>;
  avatarAttributes: FormControl<IBooks['avatarAttributes']>;
  layerGroups: FormControl<IBooks['layerGroups']>;
  selections: FormControl<IBooks['selections']>;
};

export type BooksFormGroup = FormGroup<BooksFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BooksFormService {
  createBooksFormGroup(books: BooksFormGroupInput = { id: null }): BooksFormGroup {
    const booksRawValue = {
      ...this.getFormDefaults(),
      ...books,
    };
    return new FormGroup<BooksFormGroupContent>({
      id: new FormControl(
        { value: booksRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(booksRawValue.code),
      name: new FormControl(booksRawValue.name, {
        validators: [Validators.required],
      }),
      title: new FormControl(booksRawValue.title, {
        validators: [Validators.required],
      }),
      subTitle: new FormControl(booksRawValue.subTitle),
      author: new FormControl(booksRawValue.author),
      isActive: new FormControl(booksRawValue.isActive, {
        validators: [Validators.required],
      }),
      noOfPages: new FormControl(booksRawValue.noOfPages),
      storeImg: new FormControl(booksRawValue.storeImg),
      pageSize: new FormControl(booksRawValue.pageSize),
      user: new FormControl(booksRawValue.user),
      booksPages: new FormControl(booksRawValue.booksPages ?? []),
      priceRelatedOptions: new FormControl(booksRawValue.priceRelatedOptions ?? []),
      booksRelatedOptions: new FormControl(booksRawValue.booksRelatedOptions ?? []),
      booksAttributes: new FormControl(booksRawValue.booksAttributes ?? []),
      booksVariables: new FormControl(booksRawValue.booksVariables ?? []),
      avatarAttributes: new FormControl(booksRawValue.avatarAttributes ?? []),
      layerGroups: new FormControl(booksRawValue.layerGroups ?? []),
      selections: new FormControl(booksRawValue.selections ?? []),
    });
  }

  getBooks(form: BooksFormGroup): IBooks | NewBooks {
    return form.getRawValue() as IBooks | NewBooks;
  }

  resetForm(form: BooksFormGroup, books: BooksFormGroupInput): void {
    const booksRawValue = { ...this.getFormDefaults(), ...books };
    form.reset(
      {
        ...booksRawValue,
        id: { value: booksRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BooksFormDefaults {
    return {
      id: null,
      isActive: false,
      booksPages: [],
      priceRelatedOptions: [],
      booksRelatedOptions: [],
      booksAttributes: [],
      booksVariables: [],
      avatarAttributes: [],
      layerGroups: [],
      selections: [],
    };
  }
}
