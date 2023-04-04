import { IOrgProperty, NewOrgProperty } from './org-property.model';

export const sampleWithRequiredData: IOrgProperty = {
  id: 81579,
};

export const sampleWithPartialData: IOrgProperty = {
  id: 69268,
  name: 'deposit',
};

export const sampleWithFullData: IOrgProperty = {
  id: 26083,
  name: 'JBOD',
  description: 'connecting transmitter',
  isActive: true,
};

export const sampleWithNewData: NewOrgProperty = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
