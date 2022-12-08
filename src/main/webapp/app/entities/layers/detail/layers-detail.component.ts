import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILayers } from '../layers.model';

@Component({
  selector: 'jhi-layers-detail',
  templateUrl: './layers-detail.component.html',
})
export class LayersDetailComponent implements OnInit {
  layers: ILayers | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ layers }) => {
      this.layers = layers;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
