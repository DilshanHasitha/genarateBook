import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SelectedOptionDetailsFormService, SelectedOptionDetailsFormGroup } from './selected-option-details-form.service';
import { ISelectedOptionDetails } from '../selected-option-details.model';
import { SelectedOptionDetailsService } from '../service/selected-option-details.service';

@Component({
  selector: 'jhi-selected-option-details-update',
  templateUrl: './selected-option-details-update.component.html',
})
export class SelectedOptionDetailsUpdateComponent implements OnInit {
  isSaving = false;
  selectedOptionDetails: ISelectedOptionDetails | null = null;

  editForm: SelectedOptionDetailsFormGroup = this.selectedOptionDetailsFormService.createSelectedOptionDetailsFormGroup();

  constructor(
    protected selectedOptionDetailsService: SelectedOptionDetailsService,
    protected selectedOptionDetailsFormService: SelectedOptionDetailsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ selectedOptionDetails }) => {
      this.selectedOptionDetails = selectedOptionDetails;
      if (selectedOptionDetails) {
        this.updateForm(selectedOptionDetails);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const selectedOptionDetails = this.selectedOptionDetailsFormService.getSelectedOptionDetails(this.editForm);
    if (selectedOptionDetails.id !== null) {
      this.subscribeToSaveResponse(this.selectedOptionDetailsService.update(selectedOptionDetails));
    } else {
      this.subscribeToSaveResponse(this.selectedOptionDetailsService.create(selectedOptionDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISelectedOptionDetails>>): void {
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

  protected updateForm(selectedOptionDetails: ISelectedOptionDetails): void {
    this.selectedOptionDetails = selectedOptionDetails;
    this.selectedOptionDetailsFormService.resetForm(this.editForm, selectedOptionDetails);
  }
}
