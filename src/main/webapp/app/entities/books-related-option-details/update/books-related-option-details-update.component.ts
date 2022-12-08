import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { BooksRelatedOptionDetailsFormService, BooksRelatedOptionDetailsFormGroup } from './books-related-option-details-form.service';
import { IBooksRelatedOptionDetails } from '../books-related-option-details.model';
import { BooksRelatedOptionDetailsService } from '../service/books-related-option-details.service';

@Component({
  selector: 'jhi-books-related-option-details-update',
  templateUrl: './books-related-option-details-update.component.html',
})
export class BooksRelatedOptionDetailsUpdateComponent implements OnInit {
  isSaving = false;
  booksRelatedOptionDetails: IBooksRelatedOptionDetails | null = null;

  editForm: BooksRelatedOptionDetailsFormGroup = this.booksRelatedOptionDetailsFormService.createBooksRelatedOptionDetailsFormGroup();

  constructor(
    protected booksRelatedOptionDetailsService: BooksRelatedOptionDetailsService,
    protected booksRelatedOptionDetailsFormService: BooksRelatedOptionDetailsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booksRelatedOptionDetails }) => {
      this.booksRelatedOptionDetails = booksRelatedOptionDetails;
      if (booksRelatedOptionDetails) {
        this.updateForm(booksRelatedOptionDetails);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const booksRelatedOptionDetails = this.booksRelatedOptionDetailsFormService.getBooksRelatedOptionDetails(this.editForm);
    if (booksRelatedOptionDetails.id !== null) {
      this.subscribeToSaveResponse(this.booksRelatedOptionDetailsService.update(booksRelatedOptionDetails));
    } else {
      this.subscribeToSaveResponse(this.booksRelatedOptionDetailsService.create(booksRelatedOptionDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooksRelatedOptionDetails>>): void {
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

  protected updateForm(booksRelatedOptionDetails: IBooksRelatedOptionDetails): void {
    this.booksRelatedOptionDetails = booksRelatedOptionDetails;
    this.booksRelatedOptionDetailsFormService.resetForm(this.editForm, booksRelatedOptionDetails);
  }
}
