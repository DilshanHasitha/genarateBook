import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrgProperty } from '../org-property.model';

@Component({
  selector: 'jhi-org-property-detail',
  templateUrl: './org-property-detail.component.html',
})
export class OrgPropertyDetailComponent implements OnInit {
  orgProperty: IOrgProperty | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orgProperty }) => {
      this.orgProperty = orgProperty;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
