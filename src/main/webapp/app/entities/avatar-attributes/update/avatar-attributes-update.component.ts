import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AvatarAttributesFormService, AvatarAttributesFormGroup } from './avatar-attributes-form.service';
import { IAvatarAttributes } from '../avatar-attributes.model';
import { AvatarAttributesService } from '../service/avatar-attributes.service';
import { IAvatarCharactor } from 'app/entities/avatar-charactor/avatar-charactor.model';
import { AvatarCharactorService } from 'app/entities/avatar-charactor/service/avatar-charactor.service';
import { IStyles } from 'app/entities/styles/styles.model';
import { StylesService } from 'app/entities/styles/service/styles.service';
import { IOptions } from 'app/entities/options/options.model';
import { OptionsService } from 'app/entities/options/service/options.service';

@Component({
  selector: 'jhi-avatar-attributes-update',
  templateUrl: './avatar-attributes-update.component.html',
})
export class AvatarAttributesUpdateComponent implements OnInit {
  isSaving = false;
  avatarAttributes: IAvatarAttributes | null = null;

  avatarCharactorsSharedCollection: IAvatarCharactor[] = [];
  stylesSharedCollection: IStyles[] = [];
  optionsSharedCollection: IOptions[] = [];

  editForm: AvatarAttributesFormGroup = this.avatarAttributesFormService.createAvatarAttributesFormGroup();

  constructor(
    protected avatarAttributesService: AvatarAttributesService,
    protected avatarAttributesFormService: AvatarAttributesFormService,
    protected avatarCharactorService: AvatarCharactorService,
    protected stylesService: StylesService,
    protected optionsService: OptionsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAvatarCharactor = (o1: IAvatarCharactor | null, o2: IAvatarCharactor | null): boolean =>
    this.avatarCharactorService.compareAvatarCharactor(o1, o2);

  compareStyles = (o1: IStyles | null, o2: IStyles | null): boolean => this.stylesService.compareStyles(o1, o2);

  compareOptions = (o1: IOptions | null, o2: IOptions | null): boolean => this.optionsService.compareOptions(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ avatarAttributes }) => {
      this.avatarAttributes = avatarAttributes;
      if (avatarAttributes) {
        this.updateForm(avatarAttributes);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const avatarAttributes = this.avatarAttributesFormService.getAvatarAttributes(this.editForm);
    if (avatarAttributes.id !== null) {
      this.subscribeToSaveResponse(this.avatarAttributesService.update(avatarAttributes));
    } else {
      this.subscribeToSaveResponse(this.avatarAttributesService.create(avatarAttributes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAvatarAttributes>>): void {
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

  protected updateForm(avatarAttributes: IAvatarAttributes): void {
    this.avatarAttributes = avatarAttributes;
    this.avatarAttributesFormService.resetForm(this.editForm, avatarAttributes);

    this.avatarCharactorsSharedCollection = this.avatarCharactorService.addAvatarCharactorToCollectionIfMissing<IAvatarCharactor>(
      this.avatarCharactorsSharedCollection,
      ...(avatarAttributes.avatarCharactors ?? [])
    );
    this.stylesSharedCollection = this.stylesService.addStylesToCollectionIfMissing<IStyles>(
      this.stylesSharedCollection,
      ...(avatarAttributes.styles ?? [])
    );
    this.optionsSharedCollection = this.optionsService.addOptionsToCollectionIfMissing<IOptions>(
      this.optionsSharedCollection,
      ...(avatarAttributes.options ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.avatarCharactorService
      .query()
      .pipe(map((res: HttpResponse<IAvatarCharactor[]>) => res.body ?? []))
      .pipe(
        map((avatarCharactors: IAvatarCharactor[]) =>
          this.avatarCharactorService.addAvatarCharactorToCollectionIfMissing<IAvatarCharactor>(
            avatarCharactors,
            ...(this.avatarAttributes?.avatarCharactors ?? [])
          )
        )
      )
      .subscribe((avatarCharactors: IAvatarCharactor[]) => (this.avatarCharactorsSharedCollection = avatarCharactors));

    this.stylesService
      .query()
      .pipe(map((res: HttpResponse<IStyles[]>) => res.body ?? []))
      .pipe(
        map((styles: IStyles[]) =>
          this.stylesService.addStylesToCollectionIfMissing<IStyles>(styles, ...(this.avatarAttributes?.styles ?? []))
        )
      )
      .subscribe((styles: IStyles[]) => (this.stylesSharedCollection = styles));

    this.optionsService
      .query()
      .pipe(map((res: HttpResponse<IOptions[]>) => res.body ?? []))
      .pipe(
        map((options: IOptions[]) =>
          this.optionsService.addOptionsToCollectionIfMissing<IOptions>(options, ...(this.avatarAttributes?.options ?? []))
        )
      )
      .subscribe((options: IOptions[]) => (this.optionsSharedCollection = options));
  }
}
