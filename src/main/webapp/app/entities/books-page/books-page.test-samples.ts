import { IBooksPage, NewBooksPage } from './books-page.model';

export const sampleWithRequiredData: IBooksPage = {
  id: 29279,
  num: 80329,
};

export const sampleWithPartialData: IBooksPage = {
  id: 50604,
  num: 82414,
};

export const sampleWithFullData: IBooksPage = {
  id: 54832,
  num: 62185,
  isActive: true,
};

export const sampleWithNewData: NewBooksPage = {
  num: 19340,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
