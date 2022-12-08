import { ILayerDetails, NewLayerDetails } from './layer-details.model';

export const sampleWithRequiredData: ILayerDetails = {
  id: 98979,
  name: 'South',
};

export const sampleWithPartialData: ILayerDetails = {
  id: 70781,
  name: 'Kentucky connecting',
};

export const sampleWithFullData: ILayerDetails = {
  id: 2678,
  name: 'override Kids Awesome',
  description: 'portals',
};

export const sampleWithNewData: NewLayerDetails = {
  name: 'Macedonia',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
