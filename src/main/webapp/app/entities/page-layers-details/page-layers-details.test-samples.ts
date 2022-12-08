import { IPageLayersDetails, NewPageLayersDetails } from './page-layers-details.model';

export const sampleWithRequiredData: IPageLayersDetails = {
  id: 61778,
  name: 'Paraguay',
};

export const sampleWithPartialData: IPageLayersDetails = {
  id: 52554,
  name: 'impactful USB',
  isActive: false,
};

export const sampleWithFullData: IPageLayersDetails = {
  id: 89862,
  name: 'Guilder',
  description: 'Horizontal',
  isActive: false,
};

export const sampleWithNewData: NewPageLayersDetails = {
  name: 'Soft Fresh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
