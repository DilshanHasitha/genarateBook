import { IBooksAttributes, NewBooksAttributes } from './books-attributes.model';

export const sampleWithRequiredData: IBooksAttributes = {
  id: 68311,
};

export const sampleWithPartialData: IBooksAttributes = {
  id: 82305,
  code: 'Dynamic Mount Direct',
  isActive: false,
};

export const sampleWithFullData: IBooksAttributes = {
  id: 97137,
  code: 'Soap payment',
  description: 'payment collaborative plum',
  isActive: true,
};

export const sampleWithNewData: NewBooksAttributes = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
