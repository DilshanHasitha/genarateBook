import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AvatarCharactorFormService, AvatarCharactorFormGroup } from './avatar-charactor-form.service';
import { IAvatarCharactor } from '../avatar-charactor.model';
import { AvatarCharactorService } from '../service/avatar-charactor.service';

@Component({
  selector: 'jhi-avatar-charactor-update',
  templateUrl: './avatar-charactor-update.component.html',
})
export class AvatarCharactorUpdateComponent implements OnInit {
  isSaving = false;
  avatarCharactor: IAvatarCharactor | null = null;

  editForm: AvatarCharactorFormGroup = this.avatarCharactorFormService.createAvatarCharactorFormGroup();

  constructor(
    protected avatarCharactorService: AvatarCharactorService,
    protected avatarCharactorFormService: AvatarCharactorFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ avatarCharactor }) => {
      this.avatarCharactor = avatarCharactor;
      if (avatarCharactor) {
        this.updateForm(avatarCharactor);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const avatarCharactor = this.avatarCharactorFormService.getAvatarCharactor(this.editForm);
    if (avatarCharactor.id !== null) {
      this.subscribeToSaveResponse(this.avatarCharactorService.update(avatarCharactor));
    } else {
      this.subscribeToSaveResponse(this.avatarCharactorService.create(avatarCharactor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAvatarCharactor>>): void {
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

  protected updateForm(avatarCharactor: IAvatarCharactor): void {
    this.avatarCharactor = avatarCharactor;
    this.avatarCharactorFormService.resetForm(this.editForm, avatarCharactor);
  }
}
