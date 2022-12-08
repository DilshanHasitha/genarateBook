import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { StylesFormService, StylesFormGroup } from './styles-form.service';
import { IStyles } from '../styles.model';
import { StylesService } from '../service/styles.service';

@Component({
  selector: 'jhi-styles-update',
  templateUrl: './styles-update.component.html',
})
export class StylesUpdateComponent implements OnInit {
  isSaving = false;
  styles: IStyles | null = null;

  editForm: StylesFormGroup = this.stylesFormService.createStylesFormGroup();

  constructor(
    protected stylesService: StylesService,
    protected stylesFormService: StylesFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ styles }) => {
      this.styles = styles;
      if (styles) {
        this.updateForm(styles);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const styles = this.stylesFormService.getStyles(this.editForm);
    if (styles.id !== null) {
      this.subscribeToSaveResponse(this.stylesService.update(styles));
    } else {
      this.subscribeToSaveResponse(this.stylesService.create(styles));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStyles>>): void {
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

  protected updateForm(styles: IStyles): void {
    this.styles = styles;
    this.stylesFormService.resetForm(this.editForm, styles);
  }
}
