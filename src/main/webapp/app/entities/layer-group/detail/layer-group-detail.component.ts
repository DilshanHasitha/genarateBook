import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILayerGroup } from '../layer-group.model';

@Component({
  selector: 'jhi-layer-group-detail',
  templateUrl: './layer-group-detail.component.html',
})
export class LayerGroupDetailComponent implements OnInit {
  layerGroup: ILayerGroup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ layerGroup }) => {
      this.layerGroup = layerGroup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
