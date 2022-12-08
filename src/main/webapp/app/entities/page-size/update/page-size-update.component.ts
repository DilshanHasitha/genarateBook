import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PageSizeFormService, PageSizeFormGroup } from './page-size-form.service';
import { IPageSize } from '../page-size.model';
import { PageSizeService } from '../service/page-size.service';

@Component({
  selector: 'jhi-page-size-update',
  templateUrl: './page-size-update.component.html',
})
export class PageSizeUpdateComponent implements OnInit {
  isSaving = false;
  pageSize: IPageSize | null = null;

  editForm: PageSizeFormGroup = this.pageSizeFormService.createPageSizeFormGroup();

  constructor(
    protected pageSizeService: PageSizeService,
    protected pageSizeFormService: PageSizeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pageSize }) => {
      this.pageSize = pageSize;
      if (pageSize) {
        this.updateForm(pageSize);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pageSize = this.pageSizeFormService.getPageSize(this.editForm);
    if (pageSize.id !== null) {
      this.subscribeToSaveResponse(this.pageSizeService.update(pageSize));
    } else {
      this.subscribeToSaveResponse(this.pageSizeService.create(pageSize));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPageSize>>): void {
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

  protected updateForm(pageSize: IPageSize): void {
    this.pageSize = pageSize;
    this.pageSizeFormService.resetForm(this.editForm, pageSize);
  }
}
