import { ISelections, NewSelections } from './selections.model';

export const sampleWithRequiredData: ISelections = {
  id: 75469,
};

export const sampleWithPartialData: ISelections = {
  id: 43770,
  avatarCode: 'input Gloves',
  optionCode: 'Refined Investor orange',
  image: '24/7 sensor Kids',
  x: 58690,
  y: 67685,
  isActive: true,
  width: 73908,
};

export const sampleWithFullData: ISelections = {
  id: 1637,
  avatarCode: 'invoice Soft RAM',
  styleCode: 'payment sky',
  optionCode: 'Luxembourg quantifying experiences',
  image: 'morph iterate',
  height: 5366,
  x: 22911,
  y: 46087,
  isActive: false,
  width: 92732,
  avatarAttributesCode: 'orange Director Regional',
};

export const sampleWithNewData: NewSelections = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
