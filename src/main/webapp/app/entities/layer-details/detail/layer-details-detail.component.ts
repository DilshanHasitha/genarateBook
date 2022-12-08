import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILayerDetails } from '../layer-details.model';

@Component({
  selector: 'jhi-layer-details-detail',
  templateUrl: './layer-details-detail.component.html',
})
export class LayerDetailsDetailComponent implements OnInit {
  layerDetails: ILayerDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ layerDetails }) => {
      this.layerDetails = layerDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
