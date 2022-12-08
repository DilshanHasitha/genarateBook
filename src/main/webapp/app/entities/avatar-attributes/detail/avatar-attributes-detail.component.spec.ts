import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AvatarAttributesDetailComponent } from './avatar-attributes-detail.component';

describe('AvatarAttributes Management Detail Component', () => {
  let comp: AvatarAttributesDetailComponent;
  let fixture: ComponentFixture<AvatarAttributesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AvatarAttributesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ avatarAttributes: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AvatarAttributesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AvatarAttributesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load avatarAttributes on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.avatarAttributes).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
