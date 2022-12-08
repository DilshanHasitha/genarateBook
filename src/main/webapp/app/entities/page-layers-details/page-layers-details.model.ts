import { IPageLayers } from 'app/entities/page-layers/page-layers.model';

export interface IPageLayersDetails {
  id: number;
  name?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  pageElements?: Pick<IPageLayers, 'id'>[] | null;
}

export type NewPageLayersDetails = Omit<IPageLayersDetails, 'id'> & { id: null };
