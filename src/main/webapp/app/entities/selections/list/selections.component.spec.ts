import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SelectionsService } from '../service/selections.service';

import { SelectionsComponent } from './selections.component';

describe('Selections Management Component', () => {
  let comp: SelectionsComponent;
  let fixture: ComponentFixture<SelectionsComponent>;
  let service: SelectionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'selections', component: SelectionsComponent }]), HttpClientTestingModule],
      declarations: [SelectionsComponent],
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
      .overrideTemplate(SelectionsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SelectionsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SelectionsService);

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
    expect(comp.selections?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to selectionsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getSelectionsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getSelectionsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
