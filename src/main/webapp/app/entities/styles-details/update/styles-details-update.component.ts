import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { StylesDetailsFormService, StylesDetailsFormGroup } from './styles-details-form.service';
import { IStylesDetails } from '../styles-details.model';
import { StylesDetailsService } from '../service/styles-details.service';

@Component({
  selector: 'jhi-styles-details-update',
  templateUrl: './styles-details-update.component.html',
})
export class StylesDetailsUpdateComponent implements OnInit {
  isSaving = false;
  stylesDetails: IStylesDetails | null = null;

  editForm: StylesDetailsFormGroup = this.stylesDetailsFormService.createStylesDetailsFormGroup();

  constructor(
    protected stylesDetailsService: StylesDetailsService,
    protected stylesDetailsFormService: StylesDetailsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stylesDetails }) => {
      this.stylesDetails = stylesDetails;
      if (stylesDetails) {
        this.updateForm(stylesDetails);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stylesDetails = this.stylesDetailsFormService.getStylesDetails(this.editForm);
    if (stylesDetails.id !== null) {
      this.subscribeToSaveResponse(this.stylesDetailsService.update(stylesDetails));
    } else {
      this.subscribeToSaveResponse(this.stylesDetailsService.create(stylesDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStylesDetails>>): void {
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

  protected updateForm(stylesDetails: IStylesDetails): void {
    this.stylesDetails = stylesDetails;
    this.stylesDetailsFormService.resetForm(this.editForm, stylesDetails);
  }
}
