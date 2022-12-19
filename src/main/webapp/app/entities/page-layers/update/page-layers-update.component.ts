import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PageLayersFormService, PageLayersFormGroup } from './page-layers-form.service';
import { IPageLayers } from '../page-layers.model';
import { PageLayersService } from '../service/page-layers.service';
import { IPageLayersDetails } from 'app/entities/page-layers-details/page-layers-details.model';
import { PageLayersDetailsService } from 'app/entities/page-layers-details/service/page-layers-details.service';

@Component({
  selector: 'jhi-page-layers-update',
  templateUrl: './page-layers-update.component.html',
})
export class PageLayersUpdateComponent implements OnInit {
  isSaving = false;
  pageLayers: IPageLayers | null = null;

  pageLayersDetailsSharedCollection: IPageLayersDetails[] = [];

  editForm: PageLayersFormGroup = this.pageLayersFormService.createPageLayersFormGroup();

  constructor(
    protected pageLayersService: PageLayersService,
    protected pageLayersFormService: PageLayersFormService,
    protected pageLayersDetailsService: PageLayersDetailsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePageLayersDetails = (o1: IPageLayersDetails | null, o2: IPageLayersDetails | null): boolean =>
    this.pageLayersDetailsService.comparePageLayersDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pageLayers }) => {
      this.pageLayers = pageLayers;
      if (pageLayers) {
        this.updateForm(pageLayers);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pageLayers = this.pageLayersFormService.getPageLayers(this.editForm);
    if (pageLayers.id !== null) {
      this.subscribeToSaveResponse(this.pageLayersService.update(pageLayers));
    } else {
      this.subscribeToSaveResponse(this.pageLayersService.create(pageLayers));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPageLayers>>): void {
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

  protected updateForm(pageLayers: IPageLayers): void {
    this.pageLayers = pageLayers;
    this.pageLayersFormService.resetForm(this.editForm, pageLayers);

    this.pageLayersDetailsSharedCollection = this.pageLayersDetailsService.addPageLayersDetailsToCollectionIfMissing<IPageLayersDetails>(
      this.pageLayersDetailsSharedCollection,
      ...(pageLayers.pageElementDetails ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pageLayersDetailsService
      .query({ size: 100 })
      .pipe(map((res: HttpResponse<IPageLayersDetails[]>) => res.body ?? []))
      .pipe(
        map((pageLayersDetails: IPageLayersDetails[]) =>
          this.pageLayersDetailsService.addPageLayersDetailsToCollectionIfMissing<IPageLayersDetails>(
            pageLayersDetails,
            ...(this.pageLayers?.pageElementDetails ?? [])
          )
        )
      )
      .subscribe((pageLayersDetails: IPageLayersDetails[]) => (this.pageLayersDetailsSharedCollection = pageLayersDetails));
  }
}
