import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAvatarCharactor, NewAvatarCharactor } from '../avatar-charactor.model';

export type PartialUpdateAvatarCharactor = Partial<IAvatarCharactor> & Pick<IAvatarCharactor, 'id'>;

export type EntityResponseType = HttpResponse<IAvatarCharactor>;
export type EntityArrayResponseType = HttpResponse<IAvatarCharactor[]>;

@Injectable({ providedIn: 'root' })
export class AvatarCharactorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/avatar-charactors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(avatarCharactor: NewAvatarCharactor): Observable<EntityResponseType> {
    return this.http.post<IAvatarCharactor>(this.resourceUrl, avatarCharactor, { observe: 'response' });
  }

  update(avatarCharactor: IAvatarCharactor): Observable<EntityResponseType> {
    return this.http.put<IAvatarCharactor>(`${this.resourceUrl}/${this.getAvatarCharactorIdentifier(avatarCharactor)}`, avatarCharactor, {
      observe: 'response',
    });
  }

  partialUpdate(avatarCharactor: PartialUpdateAvatarCharactor): Observable<EntityResponseType> {
    return this.http.patch<IAvatarCharactor>(`${this.resourceUrl}/${this.getAvatarCharactorIdentifier(avatarCharactor)}`, avatarCharactor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAvatarCharactor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAvatarCharactor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAvatarCharactorIdentifier(avatarCharactor: Pick<IAvatarCharactor, 'id'>): number {
    return avatarCharactor.id;
  }

  compareAvatarCharactor(o1: Pick<IAvatarCharactor, 'id'> | null, o2: Pick<IAvatarCharactor, 'id'> | null): boolean {
    return o1 && o2 ? this.getAvatarCharactorIdentifier(o1) === this.getAvatarCharactorIdentifier(o2) : o1 === o2;
  }

  addAvatarCharactorToCollectionIfMissing<Type extends Pick<IAvatarCharactor, 'id'>>(
    avatarCharactorCollection: Type[],
    ...avatarCharactorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const avatarCharactors: Type[] = avatarCharactorsToCheck.filter(isPresent);
    if (avatarCharactors.length > 0) {
      const avatarCharactorCollectionIdentifiers = avatarCharactorCollection.map(
        avatarCharactorItem => this.getAvatarCharactorIdentifier(avatarCharactorItem)!
      );
      const avatarCharactorsToAdd = avatarCharactors.filter(avatarCharactorItem => {
        const avatarCharactorIdentifier = this.getAvatarCharactorIdentifier(avatarCharactorItem);
        if (avatarCharactorCollectionIdentifiers.includes(avatarCharactorIdentifier)) {
          return false;
        }
        avatarCharactorCollectionIdentifiers.push(avatarCharactorIdentifier);
        return true;
      });
      return [...avatarCharactorsToAdd, ...avatarCharactorCollection];
    }
    return avatarCharactorCollection;
  }
}
