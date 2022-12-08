import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BooksFormService, BooksFormGroup } from './books-form.service';
import { IBooks } from '../books.model';
import { BooksService } from '../service/books.service';
import { IPageSize } from 'app/entities/page-size/page-size.model';
import { PageSizeService } from 'app/entities/page-size/service/page-size.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBooksPage } from 'app/entities/books-page/books-page.model';
import { BooksPageService } from 'app/entities/books-page/service/books-page.service';
import { IPriceRelatedOption } from 'app/entities/price-related-option/price-related-option.model';
import { PriceRelatedOptionService } from 'app/entities/price-related-option/service/price-related-option.service';
import { IBooksRelatedOption } from 'app/entities/books-related-option/books-related-option.model';
import { BooksRelatedOptionService } from 'app/entities/books-related-option/service/books-related-option.service';
import { IBooksAttributes } from 'app/entities/books-attributes/books-attributes.model';
import { BooksAttributesService } from 'app/entities/books-attributes/service/books-attributes.service';
import { IBooksVariables } from 'app/entities/books-variables/books-variables.model';
import { BooksVariablesService } from 'app/entities/books-variables/service/books-variables.service';
import { IAvatarAttributes } from 'app/entities/avatar-attributes/avatar-attributes.model';
import { AvatarAttributesService } from 'app/entities/avatar-attributes/service/avatar-attributes.service';
import { ILayerGroup } from 'app/entities/layer-group/layer-group.model';
import { LayerGroupService } from 'app/entities/layer-group/service/layer-group.service';

@Component({
  selector: 'jhi-books-update',
  templateUrl: './books-update.component.html',
})
export class BooksUpdateComponent implements OnInit {
  isSaving = false;
  books: IBooks | null = null;

  pageSizesSharedCollection: IPageSize[] = [];
  usersSharedCollection: IUser[] = [];
  booksPagesSharedCollection: IBooksPage[] = [];
  priceRelatedOptionsSharedCollection: IPriceRelatedOption[] = [];
  booksRelatedOptionsSharedCollection: IBooksRelatedOption[] = [];
  booksAttributesSharedCollection: IBooksAttributes[] = [];
  booksVariablesSharedCollection: IBooksVariables[] = [];
  avatarAttributesSharedCollection: IAvatarAttributes[] = [];
  layerGroupsSharedCollection: ILayerGroup[] = [];

  editForm: BooksFormGroup = this.booksFormService.createBooksFormGroup();

  constructor(
    protected booksService: BooksService,
    protected booksFormService: BooksFormService,
    protected pageSizeService: PageSizeService,
    protected userService: UserService,
    protected booksPageService: BooksPageService,
    protected priceRelatedOptionService: PriceRelatedOptionService,
    protected booksRelatedOptionService: BooksRelatedOptionService,
    protected booksAttributesService: BooksAttributesService,
    protected booksVariablesService: BooksVariablesService,
    protected avatarAttributesService: AvatarAttributesService,
    protected layerGroupService: LayerGroupService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePageSize = (o1: IPageSize | null, o2: IPageSize | null): boolean => this.pageSizeService.comparePageSize(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareBooksPage = (o1: IBooksPage | null, o2: IBooksPage | null): boolean => this.booksPageService.compareBooksPage(o1, o2);

  comparePriceRelatedOption = (o1: IPriceRelatedOption | null, o2: IPriceRelatedOption | null): boolean =>
    this.priceRelatedOptionService.comparePriceRelatedOption(o1, o2);

  compareBooksRelatedOption = (o1: IBooksRelatedOption | null, o2: IBooksRelatedOption | null): boolean =>
    this.booksRelatedOptionService.compareBooksRelatedOption(o1, o2);

  compareBooksAttributes = (o1: IBooksAttributes | null, o2: IBooksAttributes | null): boolean =>
    this.booksAttributesService.compareBooksAttributes(o1, o2);

  compareBooksVariables = (o1: IBooksVariables | null, o2: IBooksVariables | null): boolean =>
    this.booksVariablesService.compareBooksVariables(o1, o2);

  compareAvatarAttributes = (o1: IAvatarAttributes | null, o2: IAvatarAttributes | null): boolean =>
    this.avatarAttributesService.compareAvatarAttributes(o1, o2);

  compareLayerGroup = (o1: ILayerGroup | null, o2: ILayerGroup | null): boolean => this.layerGroupService.compareLayerGroup(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ books }) => {
      this.books = books;
      if (books) {
        this.updateForm(books);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const books = this.booksFormService.getBooks(this.editForm);
    if (books.id !== null) {
      this.subscribeToSaveResponse(this.booksService.update(books));
    } else {
      this.subscribeToSaveResponse(this.booksService.create(books));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooks>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(books: IBooks): void {
    this.books = books;
    this.booksFormService.resetForm(this.editForm, books);

    this.pageSizesSharedCollection = this.pageSizeService.addPageSizeToCollectionIfMissing<IPageSize>(
      this.pageSizesSharedCollection,
      books.pageSize
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, books.user);
    this.booksPagesSharedCollection = this.booksPageService.addBooksPageToCollectionIfMissing<IBooksPage>(
      this.booksPagesSharedCollection,
      ...(books.booksPages ?? [])
    );
    this.priceRelatedOptionsSharedCollection =
      this.priceRelatedOptionService.addPriceRelatedOptionToCollectionIfMissing<IPriceRelatedOption>(
        this.priceRelatedOptionsSharedCollection,
        ...(books.priceRelatedOptions ?? [])
      );
    this.booksRelatedOptionsSharedCollection =
      this.booksRelatedOptionService.addBooksRelatedOptionToCollectionIfMissing<IBooksRelatedOption>(
        this.booksRelatedOptionsSharedCollection,
        ...(books.booksRelatedOptions ?? [])
      );
    this.booksAttributesSharedCollection = this.booksAttributesService.addBooksAttributesToCollectionIfMissing<IBooksAttributes>(
      this.booksAttributesSharedCollection,
      ...(books.booksAttributes ?? [])
    );
    this.booksVariablesSharedCollection = this.booksVariablesService.addBooksVariablesToCollectionIfMissing<IBooksVariables>(
      this.booksVariablesSharedCollection,
      ...(books.booksVariables ?? [])
    );
    this.avatarAttributesSharedCollection = this.avatarAttributesService.addAvatarAttributesToCollectionIfMissing<IAvatarAttributes>(
      this.avatarAttributesSharedCollection,
      ...(books.avatarAttributes ?? [])
    );
    this.layerGroupsSharedCollection = this.layerGroupService.addLayerGroupToCollectionIfMissing<ILayerGroup>(
      this.layerGroupsSharedCollection,
      ...(books.layerGroups ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pageSizeService
      .query()
      .pipe(map((res: HttpResponse<IPageSize[]>) => res.body ?? []))
      .pipe(
        map((pageSizes: IPageSize[]) => this.pageSizeService.addPageSizeToCollectionIfMissing<IPageSize>(pageSizes, this.books?.pageSize))
      )
      .subscribe((pageSizes: IPageSize[]) => (this.pageSizesSharedCollection = pageSizes));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.books?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.booksPageService
      .query()
      .pipe(map((res: HttpResponse<IBooksPage[]>) => res.body ?? []))
      .pipe(
        map((booksPages: IBooksPage[]) =>
          this.booksPageService.addBooksPageToCollectionIfMissing<IBooksPage>(booksPages, ...(this.books?.booksPages ?? []))
        )
      )
      .subscribe((booksPages: IBooksPage[]) => (this.booksPagesSharedCollection = booksPages));

    this.priceRelatedOptionService
      .query()
      .pipe(map((res: HttpResponse<IPriceRelatedOption[]>) => res.body ?? []))
      .pipe(
        map((priceRelatedOptions: IPriceRelatedOption[]) =>
          this.priceRelatedOptionService.addPriceRelatedOptionToCollectionIfMissing<IPriceRelatedOption>(
            priceRelatedOptions,
            ...(this.books?.priceRelatedOptions ?? [])
          )
        )
      )
      .subscribe((priceRelatedOptions: IPriceRelatedOption[]) => (this.priceRelatedOptionsSharedCollection = priceRelatedOptions));

    this.booksRelatedOptionService
      .query()
      .pipe(map((res: HttpResponse<IBooksRelatedOption[]>) => res.body ?? []))
      .pipe(
        map((booksRelatedOptions: IBooksRelatedOption[]) =>
          this.booksRelatedOptionService.addBooksRelatedOptionToCollectionIfMissing<IBooksRelatedOption>(
            booksRelatedOptions,
            ...(this.books?.booksRelatedOptions ?? [])
          )
        )
      )
      .subscribe((booksRelatedOptions: IBooksRelatedOption[]) => (this.booksRelatedOptionsSharedCollection = booksRelatedOptions));

    this.booksAttributesService
      .query()
      .pipe(map((res: HttpResponse<IBooksAttributes[]>) => res.body ?? []))
      .pipe(
        map((booksAttributes: IBooksAttributes[]) =>
          this.booksAttributesService.addBooksAttributesToCollectionIfMissing<IBooksAttributes>(
            booksAttributes,
            ...(this.books?.booksAttributes ?? [])
          )
        )
      )
      .subscribe((booksAttributes: IBooksAttributes[]) => (this.booksAttributesSharedCollection = booksAttributes));

    this.booksVariablesService
      .query()
      .pipe(map((res: HttpResponse<IBooksVariables[]>) => res.body ?? []))
      .pipe(
        map((booksVariables: IBooksVariables[]) =>
          this.booksVariablesService.addBooksVariablesToCollectionIfMissing<IBooksVariables>(
            booksVariables,
            ...(this.books?.booksVariables ?? [])
          )
        )
      )
      .subscribe((booksVariables: IBooksVariables[]) => (this.booksVariablesSharedCollection = booksVariables));

    this.avatarAttributesService
      .query()
      .pipe(map((res: HttpResponse<IAvatarAttributes[]>) => res.body ?? []))
      .pipe(
        map((avatarAttributes: IAvatarAttributes[]) =>
          this.avatarAttributesService.addAvatarAttributesToCollectionIfMissing<IAvatarAttributes>(
            avatarAttributes,
            ...(this.books?.avatarAttributes ?? [])
          )
        )
      )
      .subscribe((avatarAttributes: IAvatarAttributes[]) => (this.avatarAttributesSharedCollection = avatarAttributes));

    this.layerGroupService
      .query()
      .pipe(map((res: HttpResponse<ILayerGroup[]>) => res.body ?? []))
      .pipe(
        map((layerGroups: ILayerGroup[]) =>
          this.layerGroupService.addLayerGroupToCollectionIfMissing<ILayerGroup>(layerGroups, ...(this.books?.layerGroups ?? []))
        )
      )
      .subscribe((layerGroups: ILayerGroup[]) => (this.layerGroupsSharedCollection = layerGroups));
  }
}
