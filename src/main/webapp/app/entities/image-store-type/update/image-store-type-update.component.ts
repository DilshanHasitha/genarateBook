import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ImageStoreTypeFormService, ImageStoreTypeFormGroup } from './image-store-type-form.service';
import { IImageStoreType } from '../image-store-type.model';
import { ImageStoreTypeService } from '../service/image-store-type.service';

@Component({
  selector: 'jhi-image-store-type-update',
  templateUrl: './image-store-type-update.component.html',
})
export class ImageStoreTypeUpdateComponent implements OnInit {
  isSaving = false;
  imageStoreType: IImageStoreType | null = null;

  editForm: ImageStoreTypeFormGroup = this.imageStoreTypeFormService.createImageStoreTypeFormGroup();

  constructor(
    protected imageStoreTypeService: ImageStoreTypeService,
    protected imageStoreTypeFormService: ImageStoreTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imageStoreType }) => {
      this.imageStoreType = imageStoreType;
      if (imageStoreType) {
        this.updateForm(imageStoreType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const imageStoreType = this.imageStoreTypeFormService.getImageStoreType(this.editForm);
    if (imageStoreType.id !== null) {
      this.subscribeToSaveResponse(this.imageStoreTypeService.update(imageStoreType));
    } else {
      this.subscribeToSaveResponse(this.imageStoreTypeService.create(imageStoreType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImageStoreType>>): void {
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

  protected updateForm(imageStoreType: IImageStoreType): void {
    this.imageStoreType = imageStoreType;
    this.imageStoreTypeFormService.resetForm(this.editForm, imageStoreType);
  }
}
