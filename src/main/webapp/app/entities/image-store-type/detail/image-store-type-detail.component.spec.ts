import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ImageStoreTypeDetailComponent } from './image-store-type-detail.component';

describe('ImageStoreType Management Detail Component', () => {
  let comp: ImageStoreTypeDetailComponent;
  let fixture: ComponentFixture<ImageStoreTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ImageStoreTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ imageStoreType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ImageStoreTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ImageStoreTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load imageStoreType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.imageStoreType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
