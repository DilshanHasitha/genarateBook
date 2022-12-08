import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { OptionsFormService, OptionsFormGroup } from './options-form.service';
import { IOptions } from '../options.model';
import { OptionsService } from '../service/options.service';
import { IStyles } from 'app/entities/styles/styles.model';
import { StylesService } from 'app/entities/styles/service/styles.service';

@Component({
  selector: 'jhi-options-update',
  templateUrl: './options-update.component.html',
})
export class OptionsUpdateComponent implements OnInit {
  isSaving = false;
  options: IOptions | null = null;

  stylesSharedCollection: IStyles[] = [];

  editForm: OptionsFormGroup = this.optionsFormService.createOptionsFormGroup();

  constructor(
    protected optionsService: OptionsService,
    protected optionsFormService: OptionsFormService,
    protected stylesService: StylesService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareStyles = (o1: IStyles | null, o2: IStyles | null): boolean => this.stylesService.compareStyles(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ options }) => {
      this.options = options;
      if (options) {
        this.updateForm(options);
      }

      this.loadRelationshipsOptions();
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

    this.stylesSharedCollection = this.stylesService.addStylesToCollectionIfMissing<IStyles>(
      this.stylesSharedCollection,
      ...(options.styles ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.stylesService
      .query()
      .pipe(map((res: HttpResponse<IStyles[]>) => res.body ?? []))
      .pipe(map((styles: IStyles[]) => this.stylesService.addStylesToCollectionIfMissing<IStyles>(styles, ...(this.options?.styles ?? []))))
      .subscribe((styles: IStyles[]) => (this.stylesSharedCollection = styles));
  }
}
