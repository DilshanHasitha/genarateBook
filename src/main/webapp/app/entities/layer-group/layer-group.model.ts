import { ILayers } from 'app/entities/layers/layers.model';
import { IBooks } from 'app/entities/books/books.model';

export interface ILayerGroup {
  id: number;
  code?: string | null;
  description?: string | null;
  isActive?: boolean | null;
  layers?: Pick<ILayers, 'id'>[] | null;
  books?: Pick<IBooks, 'id'>[] | null;
}

export type NewLayerGroup = Omit<ILayerGroup, 'id'> & { id: null };
