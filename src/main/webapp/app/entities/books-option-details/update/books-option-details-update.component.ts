import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BooksOptionDetailsFormService, BooksOptionDetailsFormGroup } from './books-option-details-form.service';
import { IBooksOptionDetails } from '../books-option-details.model';
import { BooksOptionDetailsService } from '../service/books-option-details.service';
import { IBooks } from 'app/entities/books/books.model';
import { BooksService } from 'app/entities/books/service/books.service';

@Component({
  selector: 'jhi-books-option-details-update',
  templateUrl: './books-option-details-update.component.html',
})
export class BooksOptionDetailsUpdateComponent implements OnInit {
  isSaving = false;
  booksOptionDetails: IBooksOptionDetails | null = null;

  booksSharedCollection: IBooks[] = [];

  editForm: BooksOptionDetailsFormGroup = this.booksOptionDetailsFormService.createBooksOptionDetailsFormGroup();

  constructor(
    protected booksOptionDetailsService: BooksOptionDetailsService,
    protected booksOptionDetailsFormService: BooksOptionDetailsFormService,
    protected booksService: BooksService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBooks = (o1: IBooks | null, o2: IBooks | null): boolean => this.booksService.compareBooks(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booksOptionDetails }) => {
      this.booksOptionDetails = booksOptionDetails;
      if (booksOptionDetails) {
        this.updateForm(booksOptionDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const booksOptionDetails = this.booksOptionDetailsFormService.getBooksOptionDetails(this.editForm);
    if (booksOptionDetails.id !== null) {
      this.subscribeToSaveResponse(this.booksOptionDetailsService.update(booksOptionDetails));
    } else {
      this.subscribeToSaveResponse(this.booksOptionDetailsService.create(booksOptionDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooksOptionDetails>>): void {
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

  protected updateForm(booksOptionDetails: IBooksOptionDetails): void {
    this.booksOptionDetails = booksOptionDetails;
    this.booksOptionDetailsFormService.resetForm(this.editForm, booksOptionDetails);

    this.booksSharedCollection = this.booksService.addBooksToCollectionIfMissing<IBooks>(
      this.booksSharedCollection,
      booksOptionDetails.books
    );
  }

  protected loadRelationshipsOptions(): void {
    this.booksService
      .query()
      .pipe(map((res: HttpResponse<IBooks[]>) => res.body ?? []))
      .pipe(map((books: IBooks[]) => this.booksService.addBooksToCollectionIfMissing<IBooks>(books, this.booksOptionDetails?.books)))
      .subscribe((books: IBooks[]) => (this.booksSharedCollection = books));
  }
}
