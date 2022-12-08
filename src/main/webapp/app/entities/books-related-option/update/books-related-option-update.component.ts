import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BooksRelatedOptionFormService, BooksRelatedOptionFormGroup } from './books-related-option-form.service';
import { IBooksRelatedOption } from '../books-related-option.model';
import { BooksRelatedOptionService } from '../service/books-related-option.service';
import { IBooksRelatedOptionDetails } from 'app/entities/books-related-option-details/books-related-option-details.model';
import { BooksRelatedOptionDetailsService } from 'app/entities/books-related-option-details/service/books-related-option-details.service';

@Component({
  selector: 'jhi-books-related-option-update',
  templateUrl: './books-related-option-update.component.html',
})
export class BooksRelatedOptionUpdateComponent implements OnInit {
  isSaving = false;
  booksRelatedOption: IBooksRelatedOption | null = null;

  booksRelatedOptionDetailsSharedCollection: IBooksRelatedOptionDetails[] = [];

  editForm: BooksRelatedOptionFormGroup = this.booksRelatedOptionFormService.createBooksRelatedOptionFormGroup();

  constructor(
    protected booksRelatedOptionService: BooksRelatedOptionService,
    protected booksRelatedOptionFormService: BooksRelatedOptionFormService,
    protected booksRelatedOptionDetailsService: BooksRelatedOptionDetailsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBooksRelatedOptionDetails = (o1: IBooksRelatedOptionDetails | null, o2: IBooksRelatedOptionDetails | null): boolean =>
    this.booksRelatedOptionDetailsService.compareBooksRelatedOptionDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booksRelatedOption }) => {
      this.booksRelatedOption = booksRelatedOption;
      if (booksRelatedOption) {
        this.updateForm(booksRelatedOption);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const booksRelatedOption = this.booksRelatedOptionFormService.getBooksRelatedOption(this.editForm);
    if (booksRelatedOption.id !== null) {
      this.subscribeToSaveResponse(this.booksRelatedOptionService.update(booksRelatedOption));
    } else {
      this.subscribeToSaveResponse(this.booksRelatedOptionService.create(booksRelatedOption));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooksRelatedOption>>): void {
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

  protected updateForm(booksRelatedOption: IBooksRelatedOption): void {
    this.booksRelatedOption = booksRelatedOption;
    this.booksRelatedOptionFormService.resetForm(this.editForm, booksRelatedOption);

    this.booksRelatedOptionDetailsSharedCollection =
      this.booksRelatedOptionDetailsService.addBooksRelatedOptionDetailsToCollectionIfMissing<IBooksRelatedOptionDetails>(
        this.booksRelatedOptionDetailsSharedCollection,
        ...(booksRelatedOption.booksRelatedOptionDetails ?? [])
      );
  }

  protected loadRelationshipsOptions(): void {
    this.booksRelatedOptionDetailsService
      .query()
      .pipe(map((res: HttpResponse<IBooksRelatedOptionDetails[]>) => res.body ?? []))
      .pipe(
        map((booksRelatedOptionDetails: IBooksRelatedOptionDetails[]) =>
          this.booksRelatedOptionDetailsService.addBooksRelatedOptionDetailsToCollectionIfMissing<IBooksRelatedOptionDetails>(
            booksRelatedOptionDetails,
            ...(this.booksRelatedOption?.booksRelatedOptionDetails ?? [])
          )
        )
      )
      .subscribe(
        (booksRelatedOptionDetails: IBooksRelatedOptionDetails[]) =>
          (this.booksRelatedOptionDetailsSharedCollection = booksRelatedOptionDetails)
      );
  }
}
