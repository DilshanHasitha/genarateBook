import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAvatarAttributes, NewAvatarAttributes } from '../avatar-attributes.model';

export type PartialUpdateAvatarAttributes = Partial<IAvatarAttributes> & Pick<IAvatarAttributes, 'id'>;

export type EntityResponseType = HttpResponse<IAvatarAttributes>;
export type EntityArrayResponseType = HttpResponse<IAvatarAttributes[]>;

@Injectable({ providedIn: 'root' })
export class AvatarAttributesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/avatar-attributes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(avatarAttributes: NewAvatarAttributes): Observable<EntityResponseType> {
    return this.http.post<IAvatarAttributes>(this.resourceUrl, avatarAttributes, { observe: 'response' });
  }

  update(avatarAttributes: IAvatarAttributes): Observable<EntityResponseType> {
    return this.http.put<IAvatarAttributes>(
      `${this.resourceUrl}/${this.getAvatarAttributesIdentifier(avatarAttributes)}`,
      avatarAttributes,
      { observe: 'response' }
    );
  }

  partialUpdate(avatarAttributes: PartialUpdateAvatarAttributes): Observable<EntityResponseType> {
    return this.http.patch<IAvatarAttributes>(
      `${this.resourceUrl}/${this.getAvatarAttributesIdentifier(avatarAttributes)}`,
      avatarAttributes,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAvatarAttributes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAvatarAttributes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAvatarAttributesIdentifier(avatarAttributes: Pick<IAvatarAttributes, 'id'>): number {
    return avatarAttributes.id;
  }

  compareAvatarAttributes(o1: Pick<IAvatarAttributes, 'id'> | null, o2: Pick<IAvatarAttributes, 'id'> | null): boolean {
    return o1 && o2 ? this.getAvatarAttributesIdentifier(o1) === this.getAvatarAttributesIdentifier(o2) : o1 === o2;
  }

  addAvatarAttributesToCollectionIfMissing<Type extends Pick<IAvatarAttributes, 'id'>>(
    avatarAttributesCollection: Type[],
    ...avatarAttributesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const avatarAttributes: Type[] = avatarAttributesToCheck.filter(isPresent);
    if (avatarAttributes.length > 0) {
      const avatarAttributesCollectionIdentifiers = avatarAttributesCollection.map(
        avatarAttributesItem => this.getAvatarAttributesIdentifier(avatarAttributesItem)!
      );
      const avatarAttributesToAdd = avatarAttributes.filter(avatarAttributesItem => {
        const avatarAttributesIdentifier = this.getAvatarAttributesIdentifier(avatarAttributesItem);
        if (avatarAttributesCollectionIdentifiers.includes(avatarAttributesIdentifier)) {
          return false;
        }
        avatarAttributesCollectionIdentifiers.push(avatarAttributesIdentifier);
        return true;
      });
      return [...avatarAttributesToAdd, ...avatarAttributesCollection];
    }
    return avatarAttributesCollection;
  }
}
