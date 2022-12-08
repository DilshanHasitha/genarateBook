import { IBooksRelatedOption, NewBooksRelatedOption } from './books-related-option.model';

export const sampleWithRequiredData: IBooksRelatedOption = {
  id: 21240,
  name: 'bandwidth Practical',
};

export const sampleWithPartialData: IBooksRelatedOption = {
  id: 31172,
  name: 'green Borders deposit',
};

export const sampleWithFullData: IBooksRelatedOption = {
  id: 91682,
  code: 'services',
  name: 'Coordinator bleeding-edge',
  isActive: false,
};

export const sampleWithNewData: NewBooksRelatedOption = {
  name: 'Cordoba',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
