import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILayers, NewLayers } from '../layers.model';

export type PartialUpdateLayers = Partial<ILayers> & Pick<ILayers, 'id'>;

export type EntityResponseType = HttpResponse<ILayers>;
export type EntityArrayResponseType = HttpResponse<ILayers[]>;

@Injectable({ providedIn: 'root' })
export class LayersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/layers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(layers: NewLayers): Observable<EntityResponseType> {
    return this.http.post<ILayers>(this.resourceUrl, layers, { observe: 'response' });
  }

  update(layers: ILayers): Observable<EntityResponseType> {
    return this.http.put<ILayers>(`${this.resourceUrl}/${this.getLayersIdentifier(layers)}`, layers, { observe: 'response' });
  }

  partialUpdate(layers: PartialUpdateLayers): Observable<EntityResponseType> {
    return this.http.patch<ILayers>(`${this.resourceUrl}/${this.getLayersIdentifier(layers)}`, layers, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILayers>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILayers[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLayersIdentifier(layers: Pick<ILayers, 'id'>): number {
    return layers.id;
  }

  compareLayers(o1: Pick<ILayers, 'id'> | null, o2: Pick<ILayers, 'id'> | null): boolean {
    return o1 && o2 ? this.getLayersIdentifier(o1) === this.getLayersIdentifier(o2) : o1 === o2;
  }

  addLayersToCollectionIfMissing<Type extends Pick<ILayers, 'id'>>(
    layersCollection: Type[],
    ...layersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const layers: Type[] = layersToCheck.filter(isPresent);
    if (layers.length > 0) {
      const layersCollectionIdentifiers = layersCollection.map(layersItem => this.getLayersIdentifier(layersItem)!);
      const layersToAdd = layers.filter(layersItem => {
        const layersIdentifier = this.getLayersIdentifier(layersItem);
        if (layersCollectionIdentifiers.includes(layersIdentifier)) {
          return false;
        }
        layersCollectionIdentifiers.push(layersIdentifier);
        return true;
      });
      return [...layersToAdd, ...layersCollection];
    }
    return layersCollection;
  }
}
