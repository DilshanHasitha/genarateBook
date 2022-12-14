import { ISelections, NewSelections } from './selections.model';

export const sampleWithRequiredData: ISelections = {
  id: 75469,
};

export const sampleWithPartialData: ISelections = {
  id: 65665,
  avatarCode: 'compress capability Refined',
  optionCode: 'Credit leverage 24/7',
  image: 'Road Card BEAC',
  x: 1637,
  y: 72278,
  isActive: true,
  width: 90272,
};

export const sampleWithFullData: ISelections = {
  id: 14123,
  avatarCode: 'cross-platform',
  styleCode: 'help-desk',
  optionCode: "d'Ivoire magenta",
  image: 'invoice Account',
  height: 8960,
  x: 51575,
  y: 40252,
  isActive: false,
  width: 21421,
  avatarAttributesCode: 'Global Multi-lateral',
  avatarStyle: 'Manager Cambridgeshire Unbranded',
};

export const sampleWithNewData: NewSelections = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
