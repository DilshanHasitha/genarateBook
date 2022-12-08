import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILayerDetails, NewLayerDetails } from '../layer-details.model';

export type PartialUpdateLayerDetails = Partial<ILayerDetails> & Pick<ILayerDetails, 'id'>;

export type EntityResponseType = HttpResponse<ILayerDetails>;
export type EntityArrayResponseType = HttpResponse<ILayerDetails[]>;

@Injectable({ providedIn: 'root' })
export class LayerDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/layer-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(layerDetails: NewLayerDetails): Observable<EntityResponseType> {
    return this.http.post<ILayerDetails>(this.resourceUrl, layerDetails, { observe: 'response' });
  }

  update(layerDetails: ILayerDetails): Observable<EntityResponseType> {
    return this.http.put<ILayerDetails>(`${this.resourceUrl}/${this.getLayerDetailsIdentifier(layerDetails)}`, layerDetails, {
      observe: 'response',
    });
  }

  partialUpdate(layerDetails: PartialUpdateLayerDetails): Observable<EntityResponseType> {
    return this.http.patch<ILayerDetails>(`${this.resourceUrl}/${this.getLayerDetailsIdentifier(layerDetails)}`, layerDetails, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILayerDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILayerDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLayerDetailsIdentifier(layerDetails: Pick<ILayerDetails, 'id'>): number {
    return layerDetails.id;
  }

  compareLayerDetails(o1: Pick<ILayerDetails, 'id'> | null, o2: Pick<ILayerDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getLayerDetailsIdentifier(o1) === this.getLayerDetailsIdentifier(o2) : o1 === o2;
  }

  addLayerDetailsToCollectionIfMissing<Type extends Pick<ILayerDetails, 'id'>>(
    layerDetailsCollection: Type[],
    ...layerDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const layerDetails: Type[] = layerDetailsToCheck.filter(isPresent);
    if (layerDetails.length > 0) {
      const layerDetailsCollectionIdentifiers = layerDetailsCollection.map(
        layerDetailsItem => this.getLayerDetailsIdentifier(layerDetailsItem)!
      );
      const layerDetailsToAdd = layerDetails.filter(layerDetailsItem => {
        const layerDetailsIdentifier = this.getLayerDetailsIdentifier(layerDetailsItem);
        if (layerDetailsCollectionIdentifiers.includes(layerDetailsIdentifier)) {
          return false;
        }
        layerDetailsCollectionIdentifiers.push(layerDetailsIdentifier);
        return true;
      });
      return [...layerDetailsToAdd, ...layerDetailsCollection];
    }
    return layerDetailsCollection;
  }
}
