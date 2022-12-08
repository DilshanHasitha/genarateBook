import { IStyles, NewStyles } from './styles.model';

export const sampleWithRequiredData: IStyles = {
  id: 78144,
  description: 'program',
};

export const sampleWithPartialData: IStyles = {
  id: 15592,
  description: 'redundant Soft',
  isActive: false,
  x: 90912,
  y: 4128,
};

export const sampleWithFullData: IStyles = {
  id: 13944,
  code: 'strategize blue generating',
  description: 'parsing Account Small',
  imgURL: 'Programmable capacitor Borders',
  isActive: true,
  width: 57712,
  height: 63328,
  x: 32141,
  y: 40827,
};

export const sampleWithNewData: NewStyles = {
  description: 'Pass Business-focused District',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
