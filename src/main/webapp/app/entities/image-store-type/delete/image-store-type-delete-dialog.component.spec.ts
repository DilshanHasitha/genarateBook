jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ImageStoreTypeService } from '../service/image-store-type.service';

import { ImageStoreTypeDeleteDialogComponent } from './image-store-type-delete-dialog.component';

describe('ImageStoreType Management Delete Component', () => {
  let comp: ImageStoreTypeDeleteDialogComponent;
  let fixture: ComponentFixture<ImageStoreTypeDeleteDialogComponent>;
  let service: ImageStoreTypeService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ImageStoreTypeDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(ImageStoreTypeDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ImageStoreTypeDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ImageStoreTypeService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
