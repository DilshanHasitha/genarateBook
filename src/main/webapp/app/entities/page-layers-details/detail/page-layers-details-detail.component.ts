import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPageLayersDetails } from '../page-layers-details.model';

@Component({
  selector: 'jhi-page-layers-details-detail',
  templateUrl: './page-layers-details-detail.component.html',
})
export class PageLayersDetailsDetailComponent implements OnInit {
  pageLayersDetails: IPageLayersDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pageLayersDetails }) => {
      this.pageLayersDetails = pageLayersDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
