import { ILayerGroup, NewLayerGroup } from './layer-group.model';

export const sampleWithRequiredData: ILayerGroup = {
  id: 53281,
  description: 'Avon Senior deposit',
};

export const sampleWithPartialData: ILayerGroup = {
  id: 51541,
  code: 'Steel',
  description: 'payment',
  isActive: true,
  imageUrl: 'explicit',
};

export const sampleWithFullData: ILayerGroup = {
  id: 68616,
  code: 'FTP',
  description: 'Future',
  isActive: false,
  imageUrl: 'Chicken Gloves Cambridgeshire',
};

export const sampleWithNewData: NewLayerGroup = {
  description: 'Fish',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
