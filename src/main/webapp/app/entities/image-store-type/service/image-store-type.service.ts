import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IImageStoreType, NewImageStoreType } from '../image-store-type.model';

export type PartialUpdateImageStoreType = Partial<IImageStoreType> & Pick<IImageStoreType, 'id'>;

export type EntityResponseType = HttpResponse<IImageStoreType>;
export type EntityArrayResponseType = HttpResponse<IImageStoreType[]>;

@Injectable({ providedIn: 'root' })
export class ImageStoreTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/image-store-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(imageStoreType: NewImageStoreType): Observable<EntityResponseType> {
    return this.http.post<IImageStoreType>(this.resourceUrl, imageStoreType, { observe: 'response' });
  }

  update(imageStoreType: IImageStoreType): Observable<EntityResponseType> {
    return this.http.put<IImageStoreType>(`${this.resourceUrl}/${this.getImageStoreTypeIdentifier(imageStoreType)}`, imageStoreType, {
      observe: 'response',
    });
  }

  partialUpdate(imageStoreType: PartialUpdateImageStoreType): Observable<EntityResponseType> {
    return this.http.patch<IImageStoreType>(`${this.resourceUrl}/${this.getImageStoreTypeIdentifier(imageStoreType)}`, imageStoreType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IImageStoreType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IImageStoreType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getImageStoreTypeIdentifier(imageStoreType: Pick<IImageStoreType, 'id'>): number {
    return imageStoreType.id;
  }

  compareImageStoreType(o1: Pick<IImageStoreType, 'id'> | null, o2: Pick<IImageStoreType, 'id'> | null): boolean {
    return o1 && o2 ? this.getImageStoreTypeIdentifier(o1) === this.getImageStoreTypeIdentifier(o2) : o1 === o2;
  }

  addImageStoreTypeToCollectionIfMissing<Type extends Pick<IImageStoreType, 'id'>>(
    imageStoreTypeCollection: Type[],
    ...imageStoreTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const imageStoreTypes: Type[] = imageStoreTypesToCheck.filter(isPresent);
    if (imageStoreTypes.length > 0) {
      const imageStoreTypeCollectionIdentifiers = imageStoreTypeCollection.map(
        imageStoreTypeItem => this.getImageStoreTypeIdentifier(imageStoreTypeItem)!
      );
      const imageStoreTypesToAdd = imageStoreTypes.filter(imageStoreTypeItem => {
        const imageStoreTypeIdentifier = this.getImageStoreTypeIdentifier(imageStoreTypeItem);
        if (imageStoreTypeCollectionIdentifiers.includes(imageStoreTypeIdentifier)) {
          return false;
        }
        imageStoreTypeCollectionIdentifiers.push(imageStoreTypeIdentifier);
        return true;
      });
      return [...imageStoreTypesToAdd, ...imageStoreTypeCollection];
    }
    return imageStoreTypeCollection;
  }
}
