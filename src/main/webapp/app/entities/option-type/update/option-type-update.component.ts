import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { OptionTypeFormService, OptionTypeFormGroup } from './option-type-form.service';
import { IOptionType } from '../option-type.model';
import { OptionTypeService } from '../service/option-type.service';

@Component({
  selector: 'jhi-option-type-update',
  templateUrl: './option-type-update.component.html',
})
export class OptionTypeUpdateComponent implements OnInit {
  isSaving = false;
  optionType: IOptionType | null = null;

  editForm: OptionTypeFormGroup = this.optionTypeFormService.createOptionTypeFormGroup();

  constructor(
    protected optionTypeService: OptionTypeService,
    protected optionTypeFormService: OptionTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ optionType }) => {
      this.optionType = optionType;
      if (optionType) {
        this.updateForm(optionType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const optionType = this.optionTypeFormService.getOptionType(this.editForm);
    if (optionType.id !== null) {
      this.subscribeToSaveResponse(this.optionTypeService.update(optionType));
    } else {
      this.subscribeToSaveResponse(this.optionTypeService.create(optionType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOptionType>>): void {
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

  protected updateForm(optionType: IOptionType): void {
    this.optionType = optionType;
    this.optionTypeFormService.resetForm(this.editForm, optionType);
  }
}
