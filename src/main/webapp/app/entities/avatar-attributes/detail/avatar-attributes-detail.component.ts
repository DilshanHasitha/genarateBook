import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAvatarAttributes } from '../avatar-attributes.model';

@Component({
  selector: 'jhi-avatar-attributes-detail',
  templateUrl: './avatar-attributes-detail.component.html',
})
export class AvatarAttributesDetailComponent implements OnInit {
  avatarAttributes: IAvatarAttributes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ avatarAttributes }) => {
      this.avatarAttributes = avatarAttributes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
