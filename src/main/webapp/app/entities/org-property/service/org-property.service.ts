import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrgProperty, NewOrgProperty } from '../org-property.model';

export type PartialUpdateOrgProperty = Partial<IOrgProperty> & Pick<IOrgProperty, 'id'>;

export type EntityResponseType = HttpResponse<IOrgProperty>;
export type EntityArrayResponseType = HttpResponse<IOrgProperty[]>;

@Injectable({ providedIn: 'root' })
export class OrgPropertyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/org-properties');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orgProperty: NewOrgProperty): Observable<EntityResponseType> {
    return this.http.post<IOrgProperty>(this.resourceUrl, orgProperty, { observe: 'response' });
  }

  update(orgProperty: IOrgProperty): Observable<EntityResponseType> {
    return this.http.put<IOrgProperty>(`${this.resourceUrl}/${this.getOrgPropertyIdentifier(orgProperty)}`, orgProperty, {
      observe: 'response',
    });
  }

  partialUpdate(orgProperty: PartialUpdateOrgProperty): Observable<EntityResponseType> {
    return this.http.patch<IOrgProperty>(`${this.resourceUrl}/${this.getOrgPropertyIdentifier(orgProperty)}`, orgProperty, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrgProperty>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrgProperty[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrgPropertyIdentifier(orgProperty: Pick<IOrgProperty, 'id'>): number {
    return orgProperty.id;
  }

  compareOrgProperty(o1: Pick<IOrgProperty, 'id'> | null, o2: Pick<IOrgProperty, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrgPropertyIdentifier(o1) === this.getOrgPropertyIdentifier(o2) : o1 === o2;
  }

  addOrgPropertyToCollectionIfMissing<Type extends Pick<IOrgProperty, 'id'>>(
    orgPropertyCollection: Type[],
    ...orgPropertiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orgProperties: Type[] = orgPropertiesToCheck.filter(isPresent);
    if (orgProperties.length > 0) {
      const orgPropertyCollectionIdentifiers = orgPropertyCollection.map(
        orgPropertyItem => this.getOrgPropertyIdentifier(orgPropertyItem)!
      );
      const orgPropertiesToAdd = orgProperties.filter(orgPropertyItem => {
        const orgPropertyIdentifier = this.getOrgPropertyIdentifier(orgPropertyItem);
        if (orgPropertyCollectionIdentifiers.includes(orgPropertyIdentifier)) {
          return false;
        }
        orgPropertyCollectionIdentifiers.push(orgPropertyIdentifier);
        return true;
      });
      return [...orgPropertiesToAdd, ...orgPropertyCollection];
    }
    return orgPropertyCollection;
  }
}
