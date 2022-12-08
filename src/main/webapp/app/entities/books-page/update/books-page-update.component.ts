import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BooksPageFormService, BooksPageFormGroup } from './books-page-form.service';
import { IBooksPage } from '../books-page.model';
import { BooksPageService } from '../service/books-page.service';
import { IPageLayers } from 'app/entities/page-layers/page-layers.model';
import { PageLayersService } from 'app/entities/page-layers/service/page-layers.service';

@Component({
  selector: 'jhi-books-page-update',
  templateUrl: './books-page-update.component.html',
})
export class BooksPageUpdateComponent implements OnInit {
  isSaving = false;
  booksPage: IBooksPage | null = null;

  pageLayersSharedCollection: IPageLayers[] = [];

  editForm: BooksPageFormGroup = this.booksPageFormService.createBooksPageFormGroup();

  constructor(
    protected booksPageService: BooksPageService,
    protected booksPageFormService: BooksPageFormService,
    protected pageLayersService: PageLayersService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePageLayers = (o1: IPageLayers | null, o2: IPageLayers | null): boolean => this.pageLayersService.comparePageLayers(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booksPage }) => {
      this.booksPage = booksPage;
      if (booksPage) {
        this.updateForm(booksPage);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const booksPage = this.booksPageFormService.getBooksPage(this.editForm);
    if (booksPage.id !== null) {
      this.subscribeToSaveResponse(this.booksPageService.update(booksPage));
    } else {
      this.subscribeToSaveResponse(this.booksPageService.create(booksPage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooksPage>>): void {
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

  protected updateForm(booksPage: IBooksPage): void {
    this.booksPage = booksPage;
    this.booksPageFormService.resetForm(this.editForm, booksPage);

    this.pageLayersSharedCollection = this.pageLayersService.addPageLayersToCollectionIfMissing<IPageLayers>(
      this.pageLayersSharedCollection,
      ...(booksPage.pageDetails ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pageLayersService
      .query()
      .pipe(map((res: HttpResponse<IPageLayers[]>) => res.body ?? []))
      .pipe(
        map((pageLayers: IPageLayers[]) =>
          this.pageLayersService.addPageLayersToCollectionIfMissing<IPageLayers>(pageLayers, ...(this.booksPage?.pageDetails ?? []))
        )
      )
      .subscribe((pageLayers: IPageLayers[]) => (this.pageLayersSharedCollection = pageLayers));
  }
}
