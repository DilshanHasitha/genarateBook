import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILayerGroup, NewLayerGroup } from '../layer-group.model';

export type PartialUpdateLayerGroup = Partial<ILayerGroup> & Pick<ILayerGroup, 'id'>;

export type EntityResponseType = HttpResponse<ILayerGroup>;
export type EntityArrayResponseType = HttpResponse<ILayerGroup[]>;

@Injectable({ providedIn: 'root' })
export class LayerGroupService {
  public exportOrderUrl = SERVER_API_URL + 'api/printReceipts';
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/layer-groups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(layerGroup: NewLayerGroup): Observable<EntityResponseType> {
    return this.http.post<ILayerGroup>(this.resourceUrl, layerGroup, { observe: 'response' });
  }

  update(layerGroup: ILayerGroup): Observable<EntityResponseType> {
    return this.http.put<ILayerGroup>(`${this.resourceUrl}/${this.getLayerGroupIdentifier(layerGroup)}`, layerGroup, {
      observe: 'response',
    });
  }

  partialUpdate(layerGroup: PartialUpdateLayerGroup): Observable<EntityResponseType> {
    return this.http.patch<ILayerGroup>(`${this.resourceUrl}/${this.getLayerGroupIdentifier(layerGroup)}`, layerGroup, {
      observe: 'response',
    });
  }
  exportPDF(): any {
    const httpOptions = {
      responseType: 'arraybuffer' as 'json',
    };
    return this.http.get<any>(this.exportOrderUrl + '?orderId=aaa&storeCode=kk', httpOptions);
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILayerGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILayerGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLayerGroupIdentifier(layerGroup: Pick<ILayerGroup, 'id'>): number {
    return layerGroup.id;
  }

  compareLayerGroup(o1: Pick<ILayerGroup, 'id'> | null, o2: Pick<ILayerGroup, 'id'> | null): boolean {
    return o1 && o2 ? this.getLayerGroupIdentifier(o1) === this.getLayerGroupIdentifier(o2) : o1 === o2;
  }

  addLayerGroupToCollectionIfMissing<Type extends Pick<ILayerGroup, 'id'>>(
    layerGroupCollection: Type[],
    ...layerGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const layerGroups: Type[] = layerGroupsToCheck.filter(isPresent);
    if (layerGroups.length > 0) {
      const layerGroupCollectionIdentifiers = layerGroupCollection.map(layerGroupItem => this.getLayerGroupIdentifier(layerGroupItem)!);
      const layerGroupsToAdd = layerGroups.filter(layerGroupItem => {
        const layerGroupIdentifier = this.getLayerGroupIdentifier(layerGroupItem);
        if (layerGroupCollectionIdentifiers.includes(layerGroupIdentifier)) {
          return false;
        }
        layerGroupCollectionIdentifiers.push(layerGroupIdentifier);
        return true;
      });
      return [...layerGroupsToAdd, ...layerGroupCollection];
    }
    return layerGroupCollection;
  }
}
