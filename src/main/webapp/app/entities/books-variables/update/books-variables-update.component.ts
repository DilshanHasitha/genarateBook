import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { BooksVariablesFormService, BooksVariablesFormGroup } from './books-variables-form.service';
import { IBooksVariables } from '../books-variables.model';
import { BooksVariablesService } from '../service/books-variables.service';

@Component({
  selector: 'jhi-books-variables-update',
  templateUrl: './books-variables-update.component.html',
})
export class BooksVariablesUpdateComponent implements OnInit {
  isSaving = false;
  booksVariables: IBooksVariables | null = null;

  editForm: BooksVariablesFormGroup = this.booksVariablesFormService.createBooksVariablesFormGroup();

  constructor(
    protected booksVariablesService: BooksVariablesService,
    protected booksVariablesFormService: BooksVariablesFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booksVariables }) => {
      this.booksVariables = booksVariables;
      if (booksVariables) {
        this.updateForm(booksVariables);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const booksVariables = this.booksVariablesFormService.getBooksVariables(this.editForm);
    if (booksVariables.id !== null) {
      this.subscribeToSaveResponse(this.booksVariablesService.update(booksVariables));
    } else {
      this.subscribeToSaveResponse(this.booksVariablesService.create(booksVariables));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooksVariables>>): void {
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

  protected updateForm(booksVariables: IBooksVariables): void {
    this.booksVariables = booksVariables;
    this.booksVariablesFormService.resetForm(this.editForm, booksVariables);
  }
}
