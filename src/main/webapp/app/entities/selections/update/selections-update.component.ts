import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SelectionsFormService, SelectionsFormGroup } from './selections-form.service';
import { ISelections } from '../selections.model';
import { SelectionsService } from '../service/selections.service';

@Component({
  selector: 'jhi-selections-update',
  templateUrl: './selections-update.component.html',
})
export class SelectionsUpdateComponent implements OnInit {
  isSaving = false;
  selections: ISelections | null = null;

  editForm: SelectionsFormGroup = this.selectionsFormService.createSelectionsFormGroup();

  constructor(
    protected selectionsService: SelectionsService,
    protected selectionsFormService: SelectionsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ selections }) => {
      this.selections = selections;
      if (selections) {
        this.updateForm(selections);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const selections = this.selectionsFormService.getSelections(this.editForm);
    if (selections.id !== null) {
      this.subscribeToSaveResponse(this.selectionsService.update(selections));
    } else {
      this.subscribeToSaveResponse(this.selectionsService.create(selections));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISelections>>): void {
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

  protected updateForm(selections: ISelections): void {
    this.selections = selections;
    this.selectionsFormService.resetForm(this.editForm, selections);
  }
}
