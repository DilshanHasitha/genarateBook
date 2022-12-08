jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AvatarCharactorService } from '../service/avatar-charactor.service';

import { AvatarCharactorDeleteDialogComponent } from './avatar-charactor-delete-dialog.component';

describe('AvatarCharactor Management Delete Component', () => {
  let comp: AvatarCharactorDeleteDialogComponent;
  let fixture: ComponentFixture<AvatarCharactorDeleteDialogComponent>;
  let service: AvatarCharactorService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AvatarCharactorDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(AvatarCharactorDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AvatarCharactorDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AvatarCharactorService);
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
