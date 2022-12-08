import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPageLayers } from '../page-layers.model';

@Component({
  selector: 'jhi-page-layers-detail',
  templateUrl: './page-layers-detail.component.html',
})
export class PageLayersDetailComponent implements OnInit {
  pageLayers: IPageLayers | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pageLayers }) => {
      this.pageLayers = pageLayers;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
