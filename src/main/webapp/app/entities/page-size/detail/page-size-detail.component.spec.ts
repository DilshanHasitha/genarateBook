import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PageSizeDetailComponent } from './page-size-detail.component';

describe('PageSize Management Detail Component', () => {
  let comp: PageSizeDetailComponent;
  let fixture: ComponentFixture<PageSizeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PageSizeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pageSize: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PageSizeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PageSizeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pageSize on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pageSize).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
