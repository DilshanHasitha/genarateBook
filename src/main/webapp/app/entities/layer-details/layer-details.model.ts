import { ILayers } from 'app/entities/layers/layers.model';

export interface ILayerDetails {
  id: number;
  name?: string | null;
  description?: string | null;
  layers?: Pick<ILayers, 'id'>[] | null;
}

export type NewLayerDetails = Omit<ILayerDetails, 'id'> & { id: null };
