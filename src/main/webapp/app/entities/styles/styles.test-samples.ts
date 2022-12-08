import { IStyles, NewStyles } from './styles.model';

export const sampleWithRequiredData: IStyles = {
  id: 78144,
  description: 'program',
};

export const sampleWithPartialData: IStyles = {
  id: 23521,
  description: 'Usability',
  isActive: true,
};

export const sampleWithFullData: IStyles = {
  id: 81174,
  code: 'platforms Florida',
  description: 'Soft',
  imgURL: 'overriding transmitting',
  isActive: false,
};

export const sampleWithNewData: NewStyles = {
  description: 'parsing Account Small',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
