import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FontFamilyFormService, FontFamilyFormGroup } from './font-family-form.service';
import { IFontFamily } from '../font-family.model';
import { FontFamilyService } from '../service/font-family.service';

@Component({
  selector: 'jhi-font-family-update',
  templateUrl: './font-family-update.component.html',
})
export class FontFamilyUpdateComponent implements OnInit {
  isSaving = false;
  fontFamily: IFontFamily | null = null;

  editForm: FontFamilyFormGroup = this.fontFamilyFormService.createFontFamilyFormGroup();

  constructor(
    protected fontFamilyService: FontFamilyService,
    protected fontFamilyFormService: FontFamilyFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fontFamily }) => {
      this.fontFamily = fontFamily;
      if (fontFamily) {
        this.updateForm(fontFamily);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fontFamily = this.fontFamilyFormService.getFontFamily(this.editForm);
    if (fontFamily.id !== null) {
      this.subscribeToSaveResponse(this.fontFamilyService.update(fontFamily));
    } else {
      this.subscribeToSaveResponse(this.fontFamilyService.create(fontFamily));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFontFamily>>): void {
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

  protected updateForm(fontFamily: IFontFamily): void {
    this.fontFamily = fontFamily;
    this.fontFamilyFormService.resetForm(this.editForm, fontFamily);
  }
}
