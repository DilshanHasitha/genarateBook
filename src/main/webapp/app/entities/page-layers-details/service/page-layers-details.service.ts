import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPageLayersDetails, NewPageLayersDetails } from '../page-layers-details.model';

export type PartialUpdatePageLayersDetails = Partial<IPageLayersDetails> & Pick<IPageLayersDetails, 'id'>;

export type EntityResponseType = HttpResponse<IPageLayersDetails>;
export type EntityArrayResponseType = HttpResponse<IPageLayersDetails[]>;

@Injectable({ providedIn: 'root' })
export class PageLayersDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/page-layers-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pageLayersDetails: NewPageLayersDetails): Observable<EntityResponseType> {
    return this.http.post<IPageLayersDetails>(this.resourceUrl, pageLayersDetails, { observe: 'response' });
  }

  update(pageLayersDetails: IPageLayersDetails): Observable<EntityResponseType> {
    return this.http.put<IPageLayersDetails>(
      `${this.resourceUrl}/${this.getPageLayersDetailsIdentifier(pageLayersDetails)}`,
      pageLayersDetails,
      { observe: 'response' }
    );
  }

  partialUpdate(pageLayersDetails: PartialUpdatePageLayersDetails): Observable<EntityResponseType> {
    return this.http.patch<IPageLayersDetails>(
      `${this.resourceUrl}/${this.getPageLayersDetailsIdentifier(pageLayersDetails)}`,
      pageLayersDetails,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPageLayersDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPageLayersDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPageLayersDetailsIdentifier(pageLayersDetails: Pick<IPageLayersDetails, 'id'>): number {
    return pageLayersDetails.id;
  }

  comparePageLayersDetails(o1: Pick<IPageLayersDetails, 'id'> | null, o2: Pick<IPageLayersDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getPageLayersDetailsIdentifier(o1) === this.getPageLayersDetailsIdentifier(o2) : o1 === o2;
  }

  addPageLayersDetailsToCollectionIfMissing<Type extends Pick<IPageLayersDetails, 'id'>>(
    pageLayersDetailsCollection: Type[],
    ...pageLayersDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pageLayersDetails: Type[] = pageLayersDetailsToCheck.filter(isPresent);
    if (pageLayersDetails.length > 0) {
      const pageLayersDetailsCollectionIdentifiers = pageLayersDetailsCollection.map(
        pageLayersDetailsItem => this.getPageLayersDetailsIdentifier(pageLayersDetailsItem)!
      );
      const pageLayersDetailsToAdd = pageLayersDetails.filter(pageLayersDetailsItem => {
        const pageLayersDetailsIdentifier = this.getPageLayersDetailsIdentifier(pageLayersDetailsItem);
        if (pageLayersDetailsCollectionIdentifiers.includes(pageLayersDetailsIdentifier)) {
          return false;
        }
        pageLayersDetailsCollectionIdentifiers.push(pageLayersDetailsIdentifier);
        return true;
      });
      return [...pageLayersDetailsToAdd, ...pageLayersDetailsCollection];
    }
    return pageLayersDetailsCollection;
  }
}
