import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PageLayersDetailsFormService, PageLayersDetailsFormGroup } from './page-layers-details-form.service';
import { IPageLayersDetails } from '../page-layers-details.model';
import { PageLayersDetailsService } from '../service/page-layers-details.service';

@Component({
  selector: 'jhi-page-layers-details-update',
  templateUrl: './page-layers-details-update.component.html',
})
export class PageLayersDetailsUpdateComponent implements OnInit {
  isSaving = false;
  pageLayersDetails: IPageLayersDetails | null = null;

  editForm: PageLayersDetailsFormGroup = this.pageLayersDetailsFormService.createPageLayersDetailsFormGroup();

  constructor(
    protected pageLayersDetailsService: PageLayersDetailsService,
    protected pageLayersDetailsFormService: PageLayersDetailsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pageLayersDetails }) => {
      this.pageLayersDetails = pageLayersDetails;
      if (pageLayersDetails) {
        this.updateForm(pageLayersDetails);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pageLayersDetails = this.pageLayersDetailsFormService.getPageLayersDetails(this.editForm);
    if (pageLayersDetails.id !== null) {
      this.subscribeToSaveResponse(this.pageLayersDetailsService.update(pageLayersDetails));
    } else {
      this.subscribeToSaveResponse(this.pageLayersDetailsService.create(pageLayersDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPageLayersDetails>>): void {
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

  protected updateForm(pageLayersDetails: IPageLayersDetails): void {
    this.pageLayersDetails = pageLayersDetails;
    this.pageLayersDetailsFormService.resetForm(this.editForm, pageLayersDetails);
  }
}
