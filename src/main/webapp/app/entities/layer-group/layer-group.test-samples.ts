import { ILayerGroup, NewLayerGroup } from './layer-group.model';

export const sampleWithRequiredData: ILayerGroup = {
  id: 53281,
  description: 'Avon Senior deposit',
};

export const sampleWithPartialData: ILayerGroup = {
  id: 91700,
  code: 'bi-directional Krone',
  description: 'New explicit',
  isActive: true,
};

export const sampleWithFullData: ILayerGroup = {
  id: 15740,
  code: 'core Future Rapids',
  description: 'e-business haptic Engineer',
  isActive: true,
};

export const sampleWithNewData: NewLayerGroup = {
  description: 'Shoals',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
