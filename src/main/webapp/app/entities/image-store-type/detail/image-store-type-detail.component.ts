import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IImageStoreType } from '../image-store-type.model';

@Component({
  selector: 'jhi-image-store-type-detail',
  templateUrl: './image-store-type-detail.component.html',
})
export class ImageStoreTypeDetailComponent implements OnInit {
  imageStoreType: IImageStoreType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imageStoreType }) => {
      this.imageStoreType = imageStoreType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
