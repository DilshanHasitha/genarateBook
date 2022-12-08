import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { BooksAttributesFormService, BooksAttributesFormGroup } from './books-attributes-form.service';
import { IBooksAttributes } from '../books-attributes.model';
import { BooksAttributesService } from '../service/books-attributes.service';

@Component({
  selector: 'jhi-books-attributes-update',
  templateUrl: './books-attributes-update.component.html',
})
export class BooksAttributesUpdateComponent implements OnInit {
  isSaving = false;
  booksAttributes: IBooksAttributes | null = null;

  editForm: BooksAttributesFormGroup = this.booksAttributesFormService.createBooksAttributesFormGroup();

  constructor(
    protected booksAttributesService: BooksAttributesService,
    protected booksAttributesFormService: BooksAttributesFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booksAttributes }) => {
      this.booksAttributes = booksAttributes;
      if (booksAttributes) {
        this.updateForm(booksAttributes);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const booksAttributes = this.booksAttributesFormService.getBooksAttributes(this.editForm);
    if (booksAttributes.id !== null) {
      this.subscribeToSaveResponse(this.booksAttributesService.update(booksAttributes));
    } else {
      this.subscribeToSaveResponse(this.booksAttributesService.create(booksAttributes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooksAttributes>>): void {
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

  protected updateForm(booksAttributes: IBooksAttributes): void {
    this.booksAttributes = booksAttributes;
    this.booksAttributesFormService.resetForm(this.editForm, booksAttributes);
  }
}
