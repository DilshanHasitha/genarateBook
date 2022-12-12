import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStylesDetails, NewStylesDetails } from '../styles-details.model';

export type PartialUpdateStylesDetails = Partial<IStylesDetails> & Pick<IStylesDetails, 'id'>;

export type EntityResponseType = HttpResponse<IStylesDetails>;
export type EntityArrayResponseType = HttpResponse<IStylesDetails[]>;

@Injectable({ providedIn: 'root' })
export class StylesDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/styles-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(stylesDetails: NewStylesDetails): Observable<EntityResponseType> {
    return this.http.post<IStylesDetails>(this.resourceUrl, stylesDetails, { observe: 'response' });
  }

  update(stylesDetails: IStylesDetails): Observable<EntityResponseType> {
    return this.http.put<IStylesDetails>(`${this.resourceUrl}/${this.getStylesDetailsIdentifier(stylesDetails)}`, stylesDetails, {
      observe: 'response',
    });
  }

  partialUpdate(stylesDetails: PartialUpdateStylesDetails): Observable<EntityResponseType> {
    return this.http.patch<IStylesDetails>(`${this.resourceUrl}/${this.getStylesDetailsIdentifier(stylesDetails)}`, stylesDetails, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStylesDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStylesDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStylesDetailsIdentifier(stylesDetails: Pick<IStylesDetails, 'id'>): number {
    return stylesDetails.id;
  }

  compareStylesDetails(o1: Pick<IStylesDetails, 'id'> | null, o2: Pick<IStylesDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getStylesDetailsIdentifier(o1) === this.getStylesDetailsIdentifier(o2) : o1 === o2;
  }

  addStylesDetailsToCollectionIfMissing<Type extends Pick<IStylesDetails, 'id'>>(
    stylesDetailsCollection: Type[],
    ...stylesDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stylesDetails: Type[] = stylesDetailsToCheck.filter(isPresent);
    if (stylesDetails.length > 0) {
      const stylesDetailsCollectionIdentifiers = stylesDetailsCollection.map(
        stylesDetailsItem => this.getStylesDetailsIdentifier(stylesDetailsItem)!
      );
      const stylesDetailsToAdd = stylesDetails.filter(stylesDetailsItem => {
        const stylesDetailsIdentifier = this.getStylesDetailsIdentifier(stylesDetailsItem);
        if (stylesDetailsCollectionIdentifiers.includes(stylesDetailsIdentifier)) {
          return false;
        }
        stylesDetailsCollectionIdentifiers.push(stylesDetailsIdentifier);
        return true;
      });
      return [...stylesDetailsToAdd, ...stylesDetailsCollection];
    }
    return stylesDetailsCollection;
  }
}
