import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrgPropertyFormService } from './org-property-form.service';
import { OrgPropertyService } from '../service/org-property.service';
import { IOrgProperty } from '../org-property.model';

import { OrgPropertyUpdateComponent } from './org-property-update.component';

describe('OrgProperty Management Update Component', () => {
  let comp: OrgPropertyUpdateComponent;
  let fixture: ComponentFixture<OrgPropertyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orgPropertyFormService: OrgPropertyFormService;
  let orgPropertyService: OrgPropertyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrgPropertyUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OrgPropertyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrgPropertyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orgPropertyFormService = TestBed.inject(OrgPropertyFormService);
    orgPropertyService = TestBed.inject(OrgPropertyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const orgProperty: IOrgProperty = { id: 456 };

      activatedRoute.data = of({ orgProperty });
      comp.ngOnInit();

      expect(comp.orgProperty).toEqual(orgProperty);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgProperty>>();
      const orgProperty = { id: 123 };
      jest.spyOn(orgPropertyFormService, 'getOrgProperty').mockReturnValue(orgProperty);
      jest.spyOn(orgPropertyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgProperty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orgProperty }));
      saveSubject.complete();

      // THEN
      expect(orgPropertyFormService.getOrgProperty).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orgPropertyService.update).toHaveBeenCalledWith(expect.objectContaining(orgProperty));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgProperty>>();
      const orgProperty = { id: 123 };
      jest.spyOn(orgPropertyFormService, 'getOrgProperty').mockReturnValue({ id: null });
      jest.spyOn(orgPropertyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgProperty: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orgProperty }));
      saveSubject.complete();

      // THEN
      expect(orgPropertyFormService.getOrgProperty).toHaveBeenCalled();
      expect(orgPropertyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgProperty>>();
      const orgProperty = { id: 123 };
      jest.spyOn(orgPropertyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgProperty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orgPropertyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
