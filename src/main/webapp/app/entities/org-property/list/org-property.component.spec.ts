import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrgPropertyService } from '../service/org-property.service';

import { OrgPropertyComponent } from './org-property.component';

describe('OrgProperty Management Component', () => {
  let comp: OrgPropertyComponent;
  let fixture: ComponentFixture<OrgPropertyComponent>;
  let service: OrgPropertyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'org-property', component: OrgPropertyComponent }]), HttpClientTestingModule],
      declarations: [OrgPropertyComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(OrgPropertyComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrgPropertyComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrgPropertyService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.orgProperties?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to orgPropertyService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getOrgPropertyIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOrgPropertyIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
