import { ILayerDetails } from 'app/entities/layer-details/layer-details.model';
import { ILayerGroup } from 'app/entities/layer-group/layer-group.model';

export interface ILayers {
  id: number;
  layerNo?: number | null;
  isActive?: boolean | null;
  layerdetails?: Pick<ILayerDetails, 'id'>[] | null;
  layerGroups?: Pick<ILayerGroup, 'id'>[] | null;
}

export type NewLayers = Omit<ILayers, 'id'> & { id: null };
