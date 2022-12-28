import { IFontFamily, NewFontFamily } from './font-family.model';

export const sampleWithRequiredData: IFontFamily = {
  id: 75074,
};

export const sampleWithPartialData: IFontFamily = {
  id: 39428,
  url: 'http://kristoffer.name',
};

export const sampleWithFullData: IFontFamily = {
  id: 56674,
  name: 'Rubber Ouguiya',
  url: 'https://josh.info',
  isActive: false,
};

export const sampleWithNewData: NewFontFamily = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
