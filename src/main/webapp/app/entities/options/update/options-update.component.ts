import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { OptionsFormService, OptionsFormGroup } from './options-form.service';
import { IOptions } from '../options.model';
import { OptionsService } from '../service/options.service';

@Component({
  selector: 'jhi-options-update',
  templateUrl: './options-update.component.html',
})
export class OptionsUpdateComponent implements OnInit {
  isSaving = false;
  options: IOptions | null = null;

  editForm: OptionsFormGroup = this.optionsFormService.createOptionsFormGroup();

  constructor(
    protected optionsService: OptionsService,
    protected optionsFormService: OptionsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ options }) => {
      this.options = options;
      if (options) {
        this.updateForm(options);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const options = this.optionsFormService.getOptions(this.editForm);
    if (options.id !== null) {
      this.subscribeToSaveResponse(this.optionsService.update(options));
    } else {
      this.subscribeToSaveResponse(this.optionsService.create(options));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOptions>>): void {
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

  protected updateForm(options: IOptions): void {
    this.options = options;
    this.optionsFormService.resetForm(this.editForm, options);
  }
}
