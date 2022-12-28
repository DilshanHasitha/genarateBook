import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ImagesFormService, ImagesFormGroup } from './images-form.service';
import { IImages } from '../images.model';
import { ImagesService } from '../service/images.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IImageStoreType } from 'app/entities/image-store-type/image-store-type.model';
import { ImageStoreTypeService } from 'app/entities/image-store-type/service/image-store-type.service';

@Component({
  selector: 'jhi-images-update',
  templateUrl: './images-update.component.html',
})
export class ImagesUpdateComponent implements OnInit {
  isSaving = false;
  images: IImages | null = null;

  imageStoreTypesSharedCollection: IImageStoreType[] = [];

  editForm: ImagesFormGroup = this.imagesFormService.createImagesFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected imagesService: ImagesService,
    protected imagesFormService: ImagesFormService,
    protected imageStoreTypeService: ImageStoreTypeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareImageStoreType = (o1: IImageStoreType | null, o2: IImageStoreType | null): boolean =>
    this.imageStoreTypeService.compareImageStoreType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ images }) => {
      this.images = images;
      if (images) {
        this.updateForm(images);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('bookApp.error', { message: err.message })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const images = this.imagesFormService.getImages(this.editForm);
    if (images.id !== null) {
      this.subscribeToSaveResponse(this.imagesService.update(images));
    } else {
      this.subscribeToSaveResponse(this.imagesService.create(images));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImages>>): void {
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

  protected updateForm(images: IImages): void {
    this.images = images;
    this.imagesFormService.resetForm(this.editForm, images);

    this.imageStoreTypesSharedCollection = this.imageStoreTypeService.addImageStoreTypeToCollectionIfMissing<IImageStoreType>(
      this.imageStoreTypesSharedCollection,
      images.storeType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.imageStoreTypeService
      .query()
      .pipe(map((res: HttpResponse<IImageStoreType[]>) => res.body ?? []))
      .pipe(
        map((imageStoreTypes: IImageStoreType[]) =>
          this.imageStoreTypeService.addImageStoreTypeToCollectionIfMissing<IImageStoreType>(imageStoreTypes, this.images?.storeType)
        )
      )
      .subscribe((imageStoreTypes: IImageStoreType[]) => (this.imageStoreTypesSharedCollection = imageStoreTypes));
  }
}
