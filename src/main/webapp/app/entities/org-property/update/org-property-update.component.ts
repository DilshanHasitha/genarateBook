import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { OrgPropertyFormService, OrgPropertyFormGroup } from './org-property-form.service';
import { IOrgProperty } from '../org-property.model';
import { OrgPropertyService } from '../service/org-property.service';

@Component({
  selector: 'jhi-org-property-update',
  templateUrl: './org-property-update.component.html',
})
export class OrgPropertyUpdateComponent implements OnInit {
  isSaving = false;
  orgProperty: IOrgProperty | null = null;

  editForm: OrgPropertyFormGroup = this.orgPropertyFormService.createOrgPropertyFormGroup();

  constructor(
    protected orgPropertyService: OrgPropertyService,
    protected orgPropertyFormService: OrgPropertyFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orgProperty }) => {
      this.orgProperty = orgProperty;
      if (orgProperty) {
        this.updateForm(orgProperty);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orgProperty = this.orgPropertyFormService.getOrgProperty(this.editForm);
    if (orgProperty.id !== null) {
      this.subscribeToSaveResponse(this.orgPropertyService.update(orgProperty));
    } else {
      this.subscribeToSaveResponse(this.orgPropertyService.create(orgProperty));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrgProperty>>): void {
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

  protected updateForm(orgProperty: IOrgProperty): void {
    this.orgProperty = orgProperty;
    this.orgPropertyFormService.resetForm(this.editForm, orgProperty);
  }
}
